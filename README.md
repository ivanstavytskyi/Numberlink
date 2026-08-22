# NumberLink

Numberlink is a browser online game. Pick a grid (7×7 up to 11×11), connect equal numbers with paths that fill the board, no crossings.

Backend is Java 21 / Spring Boot 4. Frontend is Vite 8, vanilla JS, Bootstrap 5. Database is PostgreSQL 16. Schema changes go through Flyway (`ddl-auto=validate`). The three services run with Docker Compose.

Guest play works out of the box. Saving scores, reviews, and the settings panel need an account (cookie-based session account authentication).

https://github.com/user-attachments/assets/c14bded2-2635-45b7-b024-02de0fd23bd7

## Run

Docker with Compose v2. Ports **7000**, **8000**, and **5432** should be free.

```bash
cp .env.example .env
docker compose up -d --build
```

- Game: http://localhost:7000
- API: http://localhost:8000
- Health: http://localhost:8000/actuator/health

`db` has to be healthy before the API starts. Flyway then applies `backend/src/main/resources/db/migration/` (currently V1–V10). The frontend container waits on `/actuator/health`, then serves Vite on 7000. The browser talks to the UI on 7000 and to the API on 8000.

```bash
docker compose logs -f backend
docker compose up -d --build backend
docker compose exec db psql -U "$DB_USER" -d "$DB_NAME"
docker compose down        # keep the volume
docker compose down -v     # wipe Postgres
```

Frontend `src/` is bind-mounted, so JS/CSS/HTML reload without rebuilding the image. Java changes need a backend rebuild. The backend image runs `gradlew build -x test`.

## .env

`.env` is gitignored. Copy `.env.example`.

DB defaults (`numberlink` / `numberlink` / `numberlink`) are enough to boot and play as a guest.

Leave Google / GitHub / SMTP empty if you do not need them. Local email+password registration still works; verification mail, password reset, and OAuth will not until you fill those variables.

| Variable | Used for |
|---|---|
| `DB_NAME`, `DB_USER`, `DB_PASSWORD` | Postgres + Spring datasource |
| `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET` | Sign in / link Google |
| `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET` | Sign in / link GitHub |
| `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `MAIL_FROM` | Verification, reset, email change |

OAuth apps should redirect to the **API**, not the Vite port:

- `http://localhost:8000/login/oauth2/code/google`
- `http://localhost:8000/login/oauth2/code/github`

After a successful login the API sends the browser back to `http://localhost:7000/`.

SMTP: port 587 → `SMTP_STARTTLS=true`, `SMTP_SSL=false`. Port 465 → the opposite.

## What is in the app

**Play** (`/`) — server generates the puzzle (`/api/create-map`) and checks the finished grid (`/api/map-check`). Hints go through `/api/hint-check`. Guests get a random session name. Logged-in players can post a score: `round(10000 / seconds)`.

**Leaderboard** (`/leaderboard/`) — weekly / monthly / all-time, plus your own row if you have scores.

**Reviews** (`/reviews/`) — 1–5 rating and a comment. One rating row per user; the comment lives on that same row.

**FAQs** (`/faqs/`) — static page.

**Settings** (header, signed-in) — username, avatar (1 MB, stored under `backend/uploads/`), password change, email change with a confirm link, Google/GitHub link and unlink, TOTP 2FA (QR + setup key), session list (device / OS / browser). You can sign out one session, the others, or all of them. Password change and “sign out all” bump the session epoch so other cookies die.

**Auth** — register / login with username or email + password (bcrypt; 8+ characters, upper, lower, digit, symbol). Email verification and forgot-password tokens. Google and GitHub via Spring OAuth2. If 2FA is on, login returns `twoFactorRequired` and waits for `/api/login/2fa`. Confirm-email landing page: `/verify/`.

CORS allows `http://localhost:7000` with credentials. CSRF is currently disabled.

## Layout

```text
numberlink/
├── docker-compose.yml
├── .env.example
├── docker/          Dockerfiles for backend, frontend, postgres
├── backend/         Spring Boot (Gradle), Flyway under src/main/resources/db/migration/
├── frontend/        Vite, root = src/
└── postgres/        schema.sql only for a brand-new volume; Flyway owns later changes
```

Pages under `frontend/src/`: `index.html` (play), `leaderboard/`, `reviews/`, `faqs/`, `verify/`. Play / leaderboard / reviews / faqs each have their own `main.js` and `style.css` — same header and settings, copied.

Java package is `numberlink`.
