import 'bootstrap/dist/css/bootstrap.min.css';
import './style.css';
import * as bootstrap from 'bootstrap';

function backendOrigin() {
  return '';
}
function backendApiUrl() {
  return `${backendOrigin()}/api`;
}

/* ——— settings ——— */

/**
 * Account settings modal.
 * Profile save: PUT /api/me/profile. Avatar: PUT/DELETE /api/me/avatar (max 1 MB).
 */

function draftStorageKey() {
  return 'numberlink.settings.draft';
}

function closeIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16" aria-hidden="true"><path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/></svg>`;
}

function gearIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 4.754a3.246 3.246 0 1 0 0 6.492 3.246 3.246 0 0 0 0-6.492M5.754 8a2.246 2.246 0 1 1 4.492 0 2.246 2.246 0 0 1-4.492 0"/><path d="M9.796 1.343c-.527-1.79-3.065-1.79-3.592 0l-.094.319a.873.873 0 0 1-1.255.52l-.292-.16c-1.64-.892-3.433.902-2.54 2.541l.159.292a.873.873 0 0 1-.52 1.255l-.319.094c-1.79.527-1.79 3.065 0 3.592l.319.094a.873.873 0 0 1 .52 1.255l-.16.292c-.892 1.64.901 3.434 2.541 2.54l.292-.159a.873.873 0 0 1 1.255.52l.094.319c.527 1.79 3.065 1.79 3.592 0l.094-.319a.873.873 0 0 1 1.255-.52l.292.16c1.64.893 3.434-.902 2.54-2.541l-.159-.292a.873.873 0 0 1 .52-1.255l.319-.094c1.79-.527 1.79-3.065 0-3.592l-.319-.094a.873.873 0 0 1-.52-1.255l.16-.292c.893-1.64-.902-3.433-2.541-2.54l-.292.159a.873.873 0 0 1-1.255-.52zm-2.633.283c.246-.835 1.428-.835 1.674 0l.094.319a1.873 1.873 0 0 0 2.693 1.115l.291-.16c.764-.415 1.6.42 1.184 1.185l-.159.292a1.873 1.873 0 0 0 1.115 2.693l.319.094c.835.246.835 1.428 0 1.674l-.319.094a1.873 1.873 0 0 0-1.115 2.693l.16.291c.415.764-.42 1.6-1.185 1.184l-.291-.159a1.873 1.873 0 0 0-2.693 1.115l-.094.319c-.246.835-1.428.835-1.674 0l-.094-.319a1.873 1.873 0 0 0-2.692-1.115l-.292.16c-.764.415-1.6-.42-1.184-1.185l.159-.291A1.873 1.873 0 0 0 1.945 8.93l-.319-.094c-.835-.246-.835-1.428 0-1.674l.319-.094A1.873 1.873 0 0 0 3.06 4.377l-.16-.292c-.415-.764.42-1.6 1.185-1.184l.292.159a1.873 1.873 0 0 0 2.693-1.115z"/></svg>`;
}

function cameraIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" viewBox="0 0 16 16" aria-hidden="true"><path d="M2.5 5.5h2l1-1.5h5l1 1.5h2A1.5 1.5 0 0 1 14 7v5.5A1.5 1.5 0 0 1 12.5 14h-9A1.5 1.5 0 0 1 2 12.5V7a1.5 1.5 0 0 1 1.5-1.5z"/><circle cx="8" cy="9.25" r="2.25"/></svg>`;
}

function googleIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 48 48" aria-hidden="true"><path fill="#FFC107" d="M43.611 20.083H42V20H24v8h11.303C33.654 32.657 29.227 36 24 36c-6.627 0-12-5.373-12-12s5.373-12 12-12c3.059 0 5.842 1.154 7.961 3.039l5.657-5.657C34.046 6.053 29.268 4 24 4 12.955 4 4 12.955 4 24s8.955 20 20 20 20-8.955 20-20c0-1.341-.138-2.65-.389-3.917z"/><path fill="#FF3D00" d="M6.306 14.691l6.571 4.819C14.655 16.108 18.961 14 24 14c3.059 0 5.842 1.154 7.961 3.039l5.657-5.657C34.046 6.053 29.268 4 24 4 16.318 4 9.656 8.337 6.306 14.691z"/><path fill="#4CAF50" d="M24 44c5.166 0 9.86-1.977 13.409-5.192l-6.19-5.238C29.211 35.091 26.715 36 24 36c-5.205 0-9.62-3.317-11.283-7.946l-6.522 5.025C9.505 39.556 16.227 44 24 44z"/><path fill="#1976D2" d="M43.611 20.083H42V20H24v8h11.303a12.04 12.04 0 0 1-4.087 5.571l.003-.002 6.19 5.238C36.971 39.205 44 34 44 24c0-1.341-.138-2.65-.389-3.917z"/></svg>`;
}

function githubIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27s1.36.09 2 .27c1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.01 8.01 0 0 0 16 8c0-4.42-3.58-8-8-8"/></svg>`;
}

function oauthAccountFromUser(user, provider) {
  const list = Array.isArray(user?.oauthAccounts) ? user.oauthAccounts : [];
  const id = String(provider || '').toUpperCase();
  return list.find((item) => String(item.provider || '').toUpperCase() === id) || null;
}

function providerLabel(id) {
  return id === 'google' ? 'Google' : 'GitHub';
}

function renderProviderBlock(id, account) {
  const name = providerLabel(id);
  const icon = id === 'google' ? googleIcon() : githubIcon();
  if (account) {
    const displayName = String(account.displayName || name).trim() || name;
    const email = String(account.email || '').trim();
    const emailHtml = email
      ? `<span class="nl-settings__oauth-chip-email">${escapeHtml(email)}</span>`
      : '';
    return `
      <div class="nl-settings__provider">
        <div class="nl-settings__oauth-chip">
          <span class="nl-settings__oauth-chip-icon" aria-hidden="true">${icon}</span>
          <span class="nl-settings__oauth-chip-copy">
            <strong class="nl-settings__oauth-chip-name">${escapeHtml(displayName)}</strong>
            ${emailHtml}
          </span>
        </div>
        <button type="button" class="nl-settings__btn nl-settings__btn--ghost" data-settings-unlink="${id}" aria-label="Disconnect ${name}">
          Disconnect
        </button>
      </div>`;
  }
  return `
    <div class="nl-settings__provider">
      <span class="nl-settings__provider-icon">${icon}</span>
      <div class="nl-settings__provider-meta">
        <p class="nl-settings__provider-name">${name}</p>
        <p class="nl-settings__provider-state">Not connected</p>
      </div>
      <button type="button" class="nl-settings__btn nl-settings__btn--ghost" data-settings-link="${id}">
        Connect
      </button>
    </div>`;
}

function settingsSections() {
  return [
  { id: 'profile', label: 'Profile' },
  { id: 'preferences', label: 'Preferences' },
  { id: 'notifications', label: 'Notifications' },
  { id: 'security', label: 'Security' },
];
}

/** Live sessions from GET /api/me/sessions. */
function formatSessionLastActive(iso, current) {
  if (current) return 'Active now';
  const then = Date.parse(iso);
  if (!Number.isFinite(then)) return 'Unknown';
  const diff = Date.now() - then;
  if (diff < 60_000) return 'Active now';
  if (diff < 3_600_000) {
    const n = Math.max(1, Math.round(diff / 60_000));
    return n === 1 ? '1 minute ago' : `${n} minutes ago`;
  }
  if (diff < 86_400_000) {
    const n = Math.max(1, Math.round(diff / 3_600_000));
    return n === 1 ? '1 hour ago' : `${n} hours ago`;
  }
  if (diff < 172_800_000) return 'Yesterday';
  const n = Math.max(1, Math.round(diff / 86_400_000));
  return n === 1 ? '1 day ago' : `${n} days ago`;
}

function mapUserSessions(data) {
  const list = Array.isArray(data?.sessions) ? data.sessions : [];
  return list.map((s) => ({
    id: s.id,
    device: s.current ? 'This browser' : (s.device || 'Unknown device'),
    place: `${s.os || 'Unknown'} · ${s.browser || 'Unknown'}`,
    lastActive: formatSessionLastActive(s.lastSeenAt, Boolean(s.current)),
    current: Boolean(s.current),
  }));
}

async function fetchUserSessions() {
  try {
    const response = await fetch(`${backendApiUrl()}/me/sessions`, {
      credentials: 'include',
      headers: { Accept: 'application/json' },
    });
    const data = await response.json().catch(() => ({}));
    if (!response.ok) return [];
    return mapUserSessions(data);
  } catch (_) {
    return [];
  }
}

async function revokeUserSession(id) {
  const response = await fetch(`${backendApiUrl()}/me/sessions/${encodeURIComponent(id)}`, {
    method: 'DELETE',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not sign out that device.');
  }
  return mapUserSessions(data);
}

async function revokeOtherUserSessions() {
  const response = await fetch(`${backendApiUrl()}/me/sessions`, {
    method: 'DELETE',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not sign out other devices.');
  }
  return mapUserSessions(data);
}

async function revokeAllUserSessions() {
  const response = await fetch(`${backendApiUrl()}/me/sessions/revoke-all`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  if (!response.ok && response.status !== 204) {
    const data = await response.json().catch(() => ({}));
    throw new Error(data.message || 'Could not sign out all devices.');
  }
}

function settingsState() {
  if (!settingsState._value) {
    settingsState._value = {
  user: null,
  section: 'profile',
  draft: null,
  onLocalProfileChange: null,
      sessions: [],
};
  }
  return settingsState._value;
}

function prefersReducedMotion() {
  return window.matchMedia('(prefers-reduced-motion: reduce)').matches;
}

function wait(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function escapeHtml(value) {
  return String(value)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

function userInitials(username = '') {
  const cleaned = String(username).trim();
  if (!cleaned) return '?';
  const parts = cleaned.split(/[\s._-]+/).filter(Boolean);
  if (parts.length >= 2) {
    return (parts[0][0] + parts[1][0]).toUpperCase();
  }
  return cleaned.slice(0, 2).toUpperCase();
}

function readDraft(userId) {
  try {
    const raw = localStorage.getItem(draftStorageKey());
    if (!raw) return null;
    const parsed = JSON.parse(raw);
    if (parsed?.userId && userId && parsed.userId !== userId) return null;
    return parsed;
  } catch (_) {
    return null;
  }
}

function writeDraft(draft) {
  try {
    localStorage.setItem(draftStorageKey(), JSON.stringify(draft));
  } catch (_) {
    /* ignore quota */
  }
}

function buildDraftFromUser(user) {
  const saved = readDraft(user?.id);
  const sound = localStorage.getItem('sound');
  const widthRaw = Number(localStorage.getItem('field_width'));
  const heightRaw = Number(localStorage.getItem('field_height'));
  return {
    userId: user?.id || null,
    username: saved?.username ?? user?.username ?? '',
    email: user?.email ?? '',
    hasPassword: Boolean(user?.hasPassword),
    emailManagedBy: user?.emailManagedBy || null,
    canEditEmail: user?.canEditEmail !== false && !user?.pendingEmail,
    pendingEmail: user?.pendingEmail || '',
    pendingExpiresAt: user?.pendingExpiresAt || null,
    avatarUrl: user?.avatarUrl || saved?.avatarUrl || null,
    googleAccount: oauthAccountFromUser(user, 'GOOGLE'),
    githubAccount: oauthAccountFromUser(user, 'GITHUB'),
    twoFactorEnabled: Boolean(user?.twoFactorEnabled),
    soundOn: sound !== 'muted',
    defaultWidth: widthRaw >= 7 && widthRaw <= 11 ? widthRaw : 7,
    defaultHeight: heightRaw >= 7 && heightRaw <= 11 ? heightRaw : 7,
    notifyProduct: saved?.notifyProduct ?? true,
    notifyScores: saved?.notifyScores ?? true,
    notifyReviews: saved?.notifyReviews ?? false,
  };
}

function mapSizeOptions(selected) {
  let html = '';
  for (let n = 7; n <= 11; n += 1) {
    html += `<option value="${n}" ${Number(selected) === n ? 'selected' : ''}>${n}</option>`;
  }
  return html;
}

function resolveMediaUrl(url) {
  if (!url) return null;
  if (/^(data:|blob:|https?:)/i.test(url)) return url;
  const path = url.startsWith('/') ? url : `/${url}`;
  return `${backendOrigin()}${path}`;
}

function renderAvatarHtml(draft) {
  const src = resolveMediaUrl(draft.avatarUrl);
  if (src) {
    return `<img src="${escapeHtml(src)}" alt="" />`;
  }
  return escapeHtml(userInitials(draft.username));
}

function statusSlot(id) {
  return `<p class="nl-settings__status" data-settings-status="${id}" aria-live="polite"></p>`;
}

function panelProfile(draft) {
  return `
    <section class="nl-settings__panel" data-settings-panel="profile" hidden>
      <h3 class="nl-settings__panel-title">Profile</h3>
      <p class="nl-settings__panel-lead">Photo, username, and email used across NumberLink.</p>
      <div class="nl-settings__avatar-row">
        <div class="nl-settings__avatar" data-settings-avatar>${renderAvatarHtml(draft)}</div>
        <div class="nl-settings__avatar-actions">
          <input class="nl-settings__file" id="settings-avatar-file" type="file" accept="image/png,image/jpeg,image/webp" />
          <button type="button" class="nl-settings__btn nl-settings__btn--ghost nl-settings__btn--icon" data-settings-avatar-pick>
            ${cameraIcon()}<span>Upload photo</span>
          </button>
          <button type="button" class="nl-settings__btn nl-settings__btn--ghost" data-settings-avatar-clear ${draft.avatarUrl ? '' : 'hidden'}>
            Remove
          </button>
        </div>
      </div>
      ${statusSlot('profile:avatar')}
      <form data-settings-form="profile" novalidate>
        <div class="nl-settings__field">
          <label for="settings-username">Username</label>
          <input id="settings-username" name="username" type="text" autocomplete="username" minlength="3" maxlength="32" pattern="[A-Za-z0-9_]{3,32}" value="${escapeHtml(draft.username)}" required />
        </div>
        <p class="nl-settings__hint">3–32 characters. Letters, numbers, and underscore only.</p>
        ${settingsEmailFieldsHtml(draft)}
        <div class="nl-settings__actions">
          <button type="submit" class="nl-settings__btn nl-settings__btn--primary">Save changes</button>
        </div>
        <p class="nl-settings__status" data-settings-status="profile:form" aria-live="polite"></p>
      </form>
    </section>`;
}

function panelPreferences(draft) {
  return `
    <section class="nl-settings__panel" data-settings-panel="preferences" hidden>
      <h3 class="nl-settings__panel-title">Preferences</h3>
      <p class="nl-settings__panel-lead">Gameplay options and connected sign-in accounts.</p>
      <div class="nl-settings__block">
        <h4 class="nl-settings__block-title">Gameplay</h4>
        <div class="nl-settings__switch-row">
          <div class="nl-settings__switch-copy">
            <strong>Sound effects</strong>
            <span>${draft.soundOn ? 'On' : 'Off'}</span>
          </div>
          <label class="nl-settings__switch">
            <input type="checkbox" data-settings-sound ${draft.soundOn ? 'checked' : ''} aria-label="Enable sound effects" />
            <span class="nl-settings__switch-track" aria-hidden="true"></span>
            <span class="nl-settings__switch-thumb" aria-hidden="true"></span>
          </label>
        </div>
        ${statusSlot('preferences:sound')}
      </div>
      <div class="nl-settings__block">
        <h4 class="nl-settings__block-title">Sign-in methods</h4>
        <p class="nl-settings__block-lead">Connect Google or GitHub to sign in without a password.</p>
        ${renderProviderBlock('google', draft.googleAccount)}
        ${renderProviderBlock('github', draft.githubAccount)}
        ${statusSlot('preferences:oauth')}
      </div>
      <div class="nl-settings__block">
        <h4 class="nl-settings__block-title">Default board size</h4>
        <p class="nl-settings__block-lead">Used when you open the play page next time.</p>
        <div class="nl-settings__inline-fields">
          <div class="nl-settings__field">
            <label for="settings-board-width">Width</label>
            <select class="nl-settings__select" id="settings-board-width" data-settings-board-width>
              ${mapSizeOptions(draft.defaultWidth)}
            </select>
          </div>
          <div class="nl-settings__field">
            <label for="settings-board-height">Height</label>
            <select class="nl-settings__select" id="settings-board-height" data-settings-board-height>
              ${mapSizeOptions(draft.defaultHeight)}
            </select>
          </div>
        </div>
        ${statusSlot('preferences:board')}
      </div>
    </section>`;
}

function panelNotifications(draft) {
  return `
    <section class="nl-settings__panel" data-settings-panel="notifications" hidden>
      <h3 class="nl-settings__panel-title">Notifications</h3>
      <p class="nl-settings__panel-lead">Choose which emails NumberLink can send you.</p>
      <div class="nl-settings__block">
        <h4 class="nl-settings__block-title">Email</h4>
        <div class="nl-settings__switch-row">
          <div class="nl-settings__switch-copy">
            <strong>Product updates</strong>
            <span>New features and occasional tips</span>
          </div>
          <label class="nl-settings__switch">
            <input type="checkbox" data-settings-notify="notifyProduct" ${draft.notifyProduct ? 'checked' : ''} aria-label="Product updates emails" />
            <span class="nl-settings__switch-track" aria-hidden="true"></span>
            <span class="nl-settings__switch-thumb" aria-hidden="true"></span>
          </label>
        </div>
        <div class="nl-settings__switch-row">
          <div class="nl-settings__switch-copy">
            <strong>Scores &amp; leaderboard</strong>
            <span>Personal bests and ranking changes</span>
          </div>
          <label class="nl-settings__switch">
            <input type="checkbox" data-settings-notify="notifyScores" ${draft.notifyScores ? 'checked' : ''} aria-label="Scores and leaderboard emails" />
            <span class="nl-settings__switch-track" aria-hidden="true"></span>
            <span class="nl-settings__switch-thumb" aria-hidden="true"></span>
          </label>
        </div>
        ${statusSlot('notifications:email')}
      </div>
    </section>`;
}

function twoFactorPanelHtml(draft) {
  const enabled = Boolean(draft.twoFactorEnabled);
  const setup = settingsState().twoFactorSetup;
  const disabling = Boolean(settingsState().twoFactorDisabling);
  const toggleOn = enabled || Boolean(setup) || disabling;
  const status = enabled ? 'Enabled' : (setup ? 'Finish setup' : 'Disabled');

  let extra = '';
  if (enabled && disabling) {
    extra = `
        <div class="nl-settings__setup" data-settings-2fa-setup>
          <p class="nl-settings__block-lead">Enter a code from your authenticator app to turn this off.</p>
          <div class="nl-settings__field">
            <label for="settings-2fa-code">Verification code</label>
            <input id="settings-2fa-code" type="text" inputmode="numeric" autocomplete="one-time-code" maxlength="6" placeholder="000000" />
          </div>
          <div class="nl-settings__actions">
            <button type="button" class="nl-settings__btn nl-settings__btn--danger" data-settings-2fa-confirm>Turn off</button>
          </div>
        </div>`;
  } else if (setup && !enabled) {
    const qr = setup.qrDataUrl
      ? `<div class="nl-settings__qr"><img src="${escapeHtml(setup.qrDataUrl)}" width="168" height="168" alt="QR code for your authenticator app" /></div>`
      : '';
    extra = `
        <div class="nl-settings__setup" data-settings-2fa-setup>
          <ol>
            <li>Open your authenticator app.</li>
            <li>Scan the QR code or enter the setup key.</li>
            <li>Enter the 6-digit code to finish setup.</li>
          </ol>
          <div class="nl-settings__2fa-row">
            ${qr}
            <div class="nl-settings__setup-key">
              <p class="nl-settings__setup-key-label">Setup key</p>
              <p class="nl-settings__hint">Can't scan? Enter this key in Google Authenticator, Authy, or 1Password.</p>
              <div class="nl-settings__setup-key-row">
                <code data-settings-2fa-secret>${escapeHtml(setup.secret || '')}</code>
                <button type="button" class="nl-settings__btn nl-settings__btn--ghost" data-settings-2fa-copy>Copy</button>
              </div>
            </div>
          </div>
          <div class="nl-settings__field">
            <label for="settings-2fa-code">Verification code</label>
            <input id="settings-2fa-code" type="text" inputmode="numeric" autocomplete="one-time-code" maxlength="6" placeholder="000000" />
          </div>
          <div class="nl-settings__actions">
            <button type="button" class="nl-settings__btn nl-settings__btn--primary" data-settings-2fa-confirm>Verify</button>
          </div>
        </div>`;
  } else if (enabled) {
    extra = `<p class="nl-settings__hint">Sign-in will ask for a 6-digit code from your authenticator app.</p>`;
  }

  return `
      <div class="nl-settings__block">
        <h4 class="nl-settings__block-title">Two-factor authentication</h4>
        <p class="nl-settings__block-lead">Require a code from an authenticator app after you sign in.</p>
        <div class="nl-settings__switch-row">
          <div class="nl-settings__switch-copy">
            <strong>Authenticator app</strong>
            <span>${status}</span>
          </div>
          <label class="nl-settings__switch">
            <input type="checkbox" data-settings-2fa ${toggleOn ? 'checked' : ''} aria-label="Enable two-factor authentication" />
            <span class="nl-settings__switch-track" aria-hidden="true"></span>
            <span class="nl-settings__switch-thumb" aria-hidden="true"></span>
          </label>
        </div>
        ${extra}
        ${statusSlot('security:2fa')}
      </div>`;
}

function panelSecurity(draft, sessions) {
  const rows = sessions
    .map(
      (s) => `
      <div class="nl-settings__session" data-session-id="${escapeHtml(s.id)}">
        <div class="nl-settings__session-meta">
          <p class="nl-settings__session-device">
            ${escapeHtml(s.device)}
            ${s.current ? '<span class="nl-settings__badge">This device</span>' : ''}
          </p>
          <p class="nl-settings__session-detail">${escapeHtml(s.place)} · ${escapeHtml(s.lastActive)}</p>
        </div>
        <button type="button" class="nl-settings__session-action" data-settings-revoke="${escapeHtml(s.id)}"${s.current ? ' data-settings-revoke-current' : ''}>Sign out</button>
      </div>`
    )
    .join('');

  return `
    <section class="nl-settings__panel" data-settings-panel="security" hidden>
      <h3 class="nl-settings__panel-title">Security</h3>
      <p class="nl-settings__panel-lead">Password, sessions, and two-factor authentication.</p>
      <div class="nl-settings__block">
        <h4 class="nl-settings__block-title">Password</h4>
        <p class="nl-settings__block-lead">Must include upper and lower case, a number, and a symbol.</p>
        <form data-settings-form="password" novalidate>
          <div class="nl-settings__field">
            <label for="settings-password-current">Current password</label>
            <input id="settings-password-current" name="current" type="password" autocomplete="current-password" required />
          </div>
          <div class="nl-settings__field">
            <label for="settings-password-new">New password</label>
            <input id="settings-password-new" name="newPassword" type="password" autocomplete="new-password" minlength="8" required />
          </div>
          <div class="nl-settings__field">
            <label for="settings-password-confirm">Confirm new password</label>
            <input id="settings-password-confirm" name="confirm" type="password" autocomplete="new-password" minlength="8" required />
          </div>
          <div class="nl-settings__actions">
            <button type="submit" class="nl-settings__btn nl-settings__btn--primary">Update password</button>
          </div>
        </form>
        ${statusSlot('security:password')}
      </div>
      ${twoFactorPanelHtml(draft)}
      <div class="nl-settings__block">
        <h4 class="nl-settings__block-title">Sessions</h4>
        <p class="nl-settings__block-lead">Sign out any device you do not recognize.</p>
        <div data-settings-sessions>${rows || '<p class="nl-settings__hint">No other sessions.</p>'}</div>
        <div class="nl-settings__actions nl-settings__actions--sessions">
          <button type="button" class="nl-settings__text-action" data-settings-revoke-others ${sessions.some((s) => !s.current) ? '' : 'hidden'}>
            Sign out other devices
          </button>
          <button type="button" class="nl-settings__text-action" data-settings-revoke-all>
            Sign out all devices
          </button>
        </div>
        ${statusSlot('security:sessions')}
      </div>
    </section>`;
}

function buildMarkup(draft, sessions, section) {
  const nav = settingsSections().map(
    (s) => `
      <button
        type="button"
        class="nl-settings__nav-btn"
        data-settings-nav="${s.id}"
        aria-current="${s.id === section ? 'page' : 'false'}"
      >${escapeHtml(s.label)}</button>`
  ).join('');

  return `
    <div class="nl-settings-overlay" id="nl-settings-overlay" hidden>
      <div
        class="nl-settings"
        role="dialog"
        aria-modal="true"
        aria-labelledby="nl-settings-title"
        tabindex="-1"
      >
        <div class="nl-settings__top">
          <div class="nl-settings__titles">
            <h2 class="nl-settings__title" id="nl-settings-title">Account settings</h2>
            <p class="nl-settings__lead">Manage your NumberLink account.</p>
          </div>
          <button type="button" class="nl-settings__close" data-settings-close aria-label="Close settings">${closeIcon()}</button>
        </div>
        <div class="nl-settings__body">
          <nav class="nl-settings__nav" aria-label="Settings sections">${nav}</nav>
          <div class="nl-settings__main">
            ${panelProfile(draft)}
            ${panelPreferences(draft)}
            ${panelNotifications(draft)}
            ${panelSecurity(draft, sessions)}
          </div>
        </div>
      </div>
    </div>`;
}

function ensureMounted() {
  if (document.getElementById('nl-settings-overlay')) return;
  document.body.insertAdjacentHTML('beforeend', buildMarkup(settingsState().draft || {}, settingsState().sessions, settingsState().section));
  bindOverlay(document.getElementById('nl-settings-overlay'));
}

function remountContent() {
  const overlay = document.getElementById('nl-settings-overlay');
  if (!overlay) {
    ensureMounted();
    return;
  }
  const wasOpen = overlay.classList.contains('is-open');
  overlay.remove();
  document.body.insertAdjacentHTML('beforeend', buildMarkup(settingsState().draft, settingsState().sessions, settingsState().section));
  const next = document.getElementById('nl-settings-overlay');
  bindOverlay(next);
  if (wasOpen) {
    next.hidden = false;
    next.classList.add('is-open');
    showSection(settingsState().section);
  }
}

function setStatus(target, message, isError = false) {
  const key = String(target || '');
  const panel = key.split(':')[0];
  document.querySelectorAll('[data-settings-status]').forEach((node) => {
    const id = node.getAttribute('data-settings-status') || '';
    if (id === key || (id !== panel && !id.startsWith(`${panel}:`))) return;
    if (id !== key) {
      node.textContent = '';
      node.classList.remove('is-error');
    }
  });
  const el = document.querySelector(`[data-settings-status="${key}"]`)
    || document.querySelector(`[data-settings-status="${panel}"]`);
  if (!el) return;
  el.textContent = message || '';
  el.classList.toggle('is-error', Boolean(isError && message));
  if (message) {
    el.scrollIntoView({ block: 'nearest', inline: 'nearest' });
  }
}

function showSection(id) {
  const section = id === 'signin' ? 'security' : id;
  settingsState().section = section;
  document.querySelectorAll('[data-settings-panel]').forEach((panel) => {
    panel.hidden = panel.getAttribute('data-settings-panel') !== section;
  });
  document.querySelectorAll('[data-settings-nav]').forEach((btn) => {
    const active = btn.getAttribute('data-settings-nav') === section;
    btn.setAttribute('aria-current', active ? 'page' : 'false');
  });
}

function persistDraftAndNotify() {
  writeDraft(settingsState().draft);
  if (typeof settingsState().onLocalProfileChange === 'function') {
    settingsState().onLocalProfileChange({
      username: settingsState().draft.username,
      email: settingsState().draft.email,
      avatarUrl: settingsState().draft.avatarUrl,
    });
  }
}

const AVATAR_MAX_BYTES = 1024 * 1024;

async function updateProfile({ username, email }) {
  const response = await fetch(`${backendApiUrl()}/me/profile`, {
    method: 'PUT',
    credentials: 'include',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, email }),
  });

  if (response.status === 401 || response.status === 403) {
    throw new Error('Sign in again to update your profile.');
  }

  if (!response.ok) {
    let message = 'Could not update profile.';
    try {
      const data = await response.json();
      message = data.message || data.detail || data.error || message;
    } catch (_) {}
    throw new Error(message);
  }

  return response.json();
}

async function uploadAvatarFile(file) {
  const body = new FormData();
  body.append('file', file);

  const response = await fetch(`${backendApiUrl()}/me/avatar`, {
    method: 'PUT',
    credentials: 'include',
    headers: { Accept: 'application/json' },
    body,
  });

  if (response.status === 401 || response.status === 403) {
    throw new Error('Sign in again to update your photo.');
  }

  if (!response.ok) {
    let message = 'Could not upload photo.';
    try {
      const data = await response.json();
      message = data.message || data.detail || data.error || message;
    } catch (_) {}
    throw new Error(message);
  }

  return response.json();
}

async function deleteAvatarRemote() {
  const response = await fetch(`${backendApiUrl()}/me/avatar`, {
    method: 'DELETE',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });

  if (response.status === 401 || response.status === 403) {
    throw new Error('Sign in again to remove your photo.');
  }

  if (!response.ok) {
    let message = 'Could not remove photo.';
    try {
      const data = await response.json();
      message = data.message || data.detail || data.error || message;
    } catch (_) {}
    throw new Error(message);
  }

  return response.json();
}

function isStrongPassword(password) {
  return (
    password.length >= 8 &&
    /[A-Z]/.test(password) &&
    /[a-z]/.test(password) &&
    /\d/.test(password) &&
    /[^A-Za-z0-9]/.test(password)
  );
}

function refreshSettingsAvatar(overlay) {
  const avatar = overlay.querySelector('[data-settings-avatar]');
  if (avatar) avatar.innerHTML = renderAvatarHtml(settingsState().draft);
}

async function onAvatarFileChange(overlay, event) {
  const input = event.target;
  const file = input.files?.[0];
  if (!file) return;

  const allowed = ['image/jpeg', 'image/png', 'image/webp'];
  if (!allowed.includes(file.type) || file.size > AVATAR_MAX_BYTES) {
    setStatus('profile:avatar', 'Use a PNG, JPG, or WebP under 1 MB.', true);
    input.value = '';
    return;
  }

  setStatus('profile:avatar', 'Uploading photo…');
  try {
    const profile = await uploadAvatarFile(file);
    const url = profile.avatarUrl || null;
    settingsState().draft.avatarUrl = url;
    if (settingsState().user) settingsState().user.avatarUrl = url;

    refreshSettingsAvatar(overlay);
    const clearBtn = overlay.querySelector('[data-settings-avatar-clear]');
    if (clearBtn) clearBtn.hidden = !url;

    persistDraftAndNotify();
    setStatus('profile:avatar', 'Photo updated.');
  } catch (err) {
    setStatus('profile:avatar', err.message || 'Could not upload photo.', true);
  } finally {
    input.value = '';
  }
}

async function onAvatarClear(overlay) {
  setStatus('profile:avatar', 'Removing photo…');
  try {
    await deleteAvatarRemote();
    settingsState().draft.avatarUrl = null;
    if (settingsState().user) settingsState().user.avatarUrl = null;

    refreshSettingsAvatar(overlay);

    const clearBtn = overlay.querySelector('[data-settings-avatar-clear]');
    if (clearBtn) clearBtn.hidden = true;

    const fileInput = overlay.querySelector('#settings-avatar-file');
    if (fileInput) fileInput.value = '';

    persistDraftAndNotify();
    setStatus('profile:avatar', 'Photo removed.');
  } catch (err) {
    setStatus('profile:avatar', err.message || 'Could not remove photo.', true);
  }
}


function emailPolicy(draft) {
  const pending = String(draft?.pendingEmail || '').trim();
  const current = String(draft?.email || '').trim();
  return {
    pending,
    current,
    managedByGoogle: draft?.emailManagedBy === 'GOOGLE',
    canEdit: Boolean(draft?.canEditEmail) && !pending,
    hasPassword: Boolean(draft?.hasPassword),
  };
}

function settingsEmailFieldsHtml(draft) {
  const policy = emailPolicy(draft);
  const locked = Boolean(policy.pending) || !policy.canEdit;
  const required = policy.canEdit && policy.current ? 'required' : '';
  const disabled = locked ? 'disabled' : '';
  let hint = '';
  let extra = '';
  if (policy.pending) {
    hint = `<p class="nl-settings__hint">Pending change to ${escapeHtml(policy.pending)}. Enter the code we emailed, or cancel.</p>`;
    extra = `<div class="nl-settings__actions nl-settings__actions--email">
      <button type="button" class="nl-settings__btn nl-settings__btn--primary" data-settings-email-code>Enter code</button>
      <button type="button" class="nl-settings__btn nl-settings__btn--ghost" data-settings-email-cancel>Cancel change</button>
    </div>`;
  } else if (policy.managedByGoogle) {
    hint = `<p class="nl-settings__hint">Managed by Google.</p>`;
  } else if (!policy.canEdit && policy.current) {
    hint = `<p class="nl-settings__hint">This email can’t be changed from NumberLink.</p>`;
  }

  return `
        <div class="nl-settings__field">
          <label for="settings-email">Email</label>
          <input id="settings-email" name="email" type="email" autocomplete="email" value="${escapeHtml(policy.current)}" placeholder="you@example.com" ${required} ${disabled} />
        </div>
        ${hint}
        ${extra}`;
}

async function startEmailChange({ email, password }) {
  const body = { email };
  if (password) body.password = password;
  const response = await fetch(`${backendApiUrl()}/me/email-change`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not start email change.');
  }
  return data;
}

async function confirmEmailChange(code) {
  const response = await fetch(`${backendApiUrl()}/me/email-change/confirm`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({ code }),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not confirm email.');
  }
  return data;
}

async function resendEmailChangeCode() {
  const response = await fetch(`${backendApiUrl()}/me/email-change/resend`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not resend the code.');
  }
  return data;
}

async function cancelEmailChangeFromSettings() {
  const response = await fetch(`${backendApiUrl()}/me/email-change/cancel`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not cancel the email change.');
  }
}

function applyEmailChangeFlags(draft, data) {
  if (!draft || !data) return;
  if ('email' in data && data.email != null) draft.email = data.email;
  if ('pendingEmail' in data) draft.pendingEmail = data.pendingEmail || '';
  if ('pendingExpiresAt' in data) draft.pendingExpiresAt = data.pendingExpiresAt || null;
  if ('canEditEmail' in data) draft.canEditEmail = Boolean(data.canEditEmail);
  if ('hasPassword' in data) draft.hasPassword = Boolean(data.hasPassword);
  if ('emailManagedBy' in data) draft.emailManagedBy = data.emailManagedBy || null;
}

async function refreshSettingsUserFlags() {
  const user = await fetchCurrentUser();
  if (!user || !settingsState().draft) return user;
  settingsState().user = user;
  applyEmailChangeFlags(settingsState().draft, user);
  settingsState().draft.username = settingsState().draft.username || user.username;
  settingsState().draft.email = user.email || '';
  writeDraft(settingsState().draft);
  return user;
}

function closeEmailChangeOverlays() {
  document.querySelectorAll('[data-auth-overlay="email-password"], [data-auth-overlay="email-code"], [data-auth-overlay="email-result"]').forEach((overlay) => {
    overlay.classList.remove('is-open', 'is-closing');
    overlay.hidden = true;
  });
  if (!document.querySelector('.auth-overlay.is-open') && !document.getElementById('nl-settings-overlay')?.classList.contains('is-open')) {
    document.body.style.overflow = '';
  }
}

function openEmailChangeOverlay(id) {
  const overlay = document.getElementById(id);
  if (!overlay) return null;
  document.querySelectorAll('[data-auth-overlay="email-password"], [data-auth-overlay="email-code"]').forEach((el) => {
    if (el !== overlay) {
      el.classList.remove('is-open');
      el.hidden = true;
    }
  });
  overlay.hidden = false;
  overlay.classList.add('is-open');
  document.body.style.overflow = 'hidden';
  return overlay;
}

function readOtpValue(overlay) {
  return Array.from(overlay.querySelectorAll('[data-otp-input]'))
    .map((input) => input.value.replace(/\D/g, ''))
    .join('')
    .slice(0, 6);
}

function bindOtpInputs(overlay) {
  const inputs = Array.from(overlay.querySelectorAll('[data-otp-input]'));
  if (!inputs.length || overlay.dataset.otpBound === '1') return;
  overlay.dataset.otpBound = '1';

  inputs.forEach((input, index) => {
    input.addEventListener('input', () => {
      const digit = input.value.replace(/\D/g, '').slice(-1);
      input.value = digit;
      if (digit && index < inputs.length - 1) inputs[index + 1].focus();
    });
    input.addEventListener('keydown', (e) => {
      if (e.key === 'Backspace' && !input.value && index > 0) {
        inputs[index - 1].focus();
        inputs[index - 1].value = '';
        e.preventDefault();
      }
    });
    input.addEventListener('paste', (e) => {
      const text = (e.clipboardData || window.clipboardData).getData('text').replace(/\D/g, '').slice(0, 6);
      if (!text) return;
      e.preventDefault();
      text.split('').forEach((ch, i) => {
        if (inputs[i]) inputs[i].value = ch;
      });
      inputs[Math.min(text.length, inputs.length) - 1]?.focus();
    });
  });
}

function startEmailCodeCooldown(overlay, seconds = 60) {
  const btn = overlay.querySelector('[data-email-code-resend]');
  if (!btn) return;
  clearInterval(Number(overlay.dataset.emailCodeTimer || 0));
  let left = seconds;
  const tick = () => {
    if (left <= 0) {
      clearInterval(Number(overlay.dataset.emailCodeTimer || 0));
      btn.disabled = false;
      btn.textContent = 'Resend code';
      return;
    }
    btn.disabled = true;
    btn.textContent = `Resend code in ${left}s`;
    left -= 1;
  };
  tick();
  overlay.dataset.emailCodeTimer = String(setInterval(tick, 1000));
}

function openEmailPasswordModal(newEmail) {
  const overlay = openEmailChangeOverlay('auth-overlay-email-password');
  if (!overlay) return;
  overlay.dataset.newEmail = newEmail;
  const err = overlay.querySelector('[data-auth-error]');
  if (err) err.textContent = '';
  const input = overlay.querySelector('#email-change-password');
  if (input) input.value = '';
  requestAnimationFrame(() => input?.focus());
}

function openEmailCodeModal(pendingEmail) {
  const overlay = openEmailChangeOverlay('auth-overlay-email-code');
  if (!overlay) return;
  overlay.dataset.pendingEmail = pendingEmail || '';
  const target = overlay.querySelector('[data-email-code-target]');
  if (target) target.textContent = pendingEmail || 'your new inbox';
  const err = overlay.querySelector('[data-auth-error]');
  if (err) err.textContent = '';
  overlay.querySelectorAll('[data-otp-input]').forEach((input) => { input.value = ''; });
  bindOtpInputs(overlay);
  startEmailCodeCooldown(overlay, 60);
  requestAnimationFrame(() => overlay.querySelector('[data-otp-input]')?.focus());
}

function openEmailChangeResultModal({ title, lead, error = false }) {
  closeEmailChangeOverlays();
  const overlay = document.getElementById('auth-overlay-email-result');
  if (!overlay) return;
  overlay.hidden = false;
  overlay.classList.add('is-open');
  document.body.style.overflow = 'hidden';
  const titleEl = overlay.querySelector('#auth-title-email-result');
  const leadEl = overlay.querySelector('[data-email-result-lead]');
  if (titleEl) titleEl.textContent = title;
  if (leadEl) leadEl.textContent = lead;
  overlay.classList.toggle('is-error', Boolean(error));
}

async function onEmailPasswordSubmit(event) {
  event.preventDefault();
  const overlay = event.currentTarget.closest('[data-auth-overlay]');
  const password = overlay.querySelector('#email-change-password')?.value || '';
  const newEmail = overlay.dataset.newEmail || '';
  const err = overlay.querySelector('[data-auth-error]');
  const submit = overlay.querySelector('.auth-submit');
  if (err) err.textContent = '';
  if (!password) {
    if (err) err.textContent = 'Enter your current password.';
    return;
  }
  if (submit) submit.disabled = true;
  try {
    const data = await startEmailChange({ email: newEmail, password });
    applyEmailChangeFlags(settingsState().draft, data);
    remountContent();
    showSection('profile');
    openEmailCodeModal(data.pendingEmail || newEmail);
    setStatus('profile:form', 'Check your inbox for a confirmation code.');
  } catch (error) {
    if (err) err.textContent = error.message || 'Could not confirm password.';
  } finally {
    if (submit) submit.disabled = false;
  }
}

async function onEmailCodeSubmit(event) {
  event.preventDefault();
  const overlay = event.currentTarget.closest('[data-auth-overlay]');
  const code = readOtpValue(overlay);
  const err = overlay.querySelector('[data-auth-error]');
  if (err) err.textContent = '';
  if (!/^\d{6}$/.test(code)) {
    if (err) err.textContent = 'Enter the 6-digit code.';
    return;
  }
  const submit = overlay.querySelector('.auth-submit');
  if (submit) submit.disabled = true;
  try {
    const data = await confirmEmailChange(code);
    applyEmailChangeFlags(settingsState().draft, { ...data, pendingEmail: '' });
    if (settingsState().user) {
      settingsState().user.email = data.email || settingsState().draft.email;
    }
    persistDraftAndNotify();
    remountContent();
    showSection('profile');
    closeEmailChangeOverlays();
    setStatus('profile:form', 'Email updated.');
  } catch (error) {
    if (err) err.textContent = error.message || 'Could not confirm email.';
  } finally {
    if (submit) submit.disabled = false;
  }
}

async function onEmailCodeResend(overlay) {
  const btn = overlay.querySelector('[data-email-code-resend]');
  const err = overlay.querySelector('[data-auth-error]');
  if (!btn || btn.disabled) return;
  if (err) err.textContent = '';
  try {
    const data = await resendEmailChangeCode();
    applyEmailChangeFlags(settingsState().draft, data);
    startEmailCodeCooldown(overlay, 60);
  } catch (error) {
    if (err) err.textContent = error.message || 'Could not resend the code.';
  }
}

async function onSettingsCancelEmailChange() {
  try {
    await cancelEmailChangeFromSettings();
    if (settingsState().draft) {
      settingsState().draft.pendingEmail = '';
      settingsState().draft.pendingExpiresAt = null;
      settingsState().draft.canEditEmail = Boolean(settingsState().draft.hasPassword) || !settingsState().draft.email;
    }
    await refreshSettingsUserFlags();
    remountContent();
    showSection('profile');
    setStatus('profile:form', 'Email change cancelled.');
  } catch (error) {
    setStatus('profile:form', error.message || 'Could not cancel the email change.', true);
  }
}

function consumeEmailChangeFromUrl() {
  const params = new URLSearchParams(window.location.search);
  const flag = params.get('emailChange');
  if (!flag) return;
  params.delete('emailChange');
  const next = `${window.location.pathname}${params.toString() ? `?${params}` : ''}${window.location.hash || ''}`;
  window.history.replaceState({}, '', next);
  if (flag === 'cancelled') {
    openEmailChangeResultModal({
      title: 'Email change cancelled',
      lead: 'Your NumberLink email was not changed. If you still use this account, sign in again.',
    });
  } else {
    openEmailChangeResultModal({
      title: 'This link is no longer valid',
      lead: 'The cancel link is invalid or has expired. If you still need help, sign in and check Settings.',
      error: true,
    });
  }
}

function buildEmailPasswordDialog() {
  return `
    <div class="auth-overlay auth-overlay--stack" id="auth-overlay-email-password" data-auth-overlay="email-password" hidden>
      <div class="auth-dialog" role="dialog" aria-modal="true" aria-labelledby="auth-title-email-password">
        <button type="button" class="auth-dialog__close" data-email-change-close aria-label="Close">${closeIcon()}</button>
        <h2 class="auth-dialog__title" id="auth-title-email-password">Confirm it’s you</h2>
        <p class="auth-dialog__lead">Enter your NumberLink password to send a confirmation code to the new address.</p>
        <form class="auth-form" data-email-password-form novalidate>
          <div class="auth-field">
            <label for="email-change-password">Current password</label>
            <input id="email-change-password" name="password" type="password" autocomplete="current-password" required />
          </div>
          <p class="auth-error" data-auth-error aria-live="polite"></p>
          <button type="submit" class="auth-submit">Continue</button>
        </form>
      </div>
    </div>`;
}

function buildEmailCodeDialog() {
  const boxes = Array.from({ length: 6 }, (_, i) => (
    `<input data-otp-input inputmode="numeric" autocomplete="${i === 0 ? 'one-time-code' : 'off'}" maxlength="1" aria-label="Digit ${i + 1}" />`
  )).join('');
  return `
    <div class="auth-overlay auth-overlay--stack" id="auth-overlay-email-code" data-auth-overlay="email-code" hidden>
      <div class="auth-dialog" role="dialog" aria-modal="true" aria-labelledby="auth-title-email-code">
        <button type="button" class="auth-dialog__close" data-email-change-close aria-label="Close">${closeIcon()}</button>
        <h2 class="auth-dialog__title" id="auth-title-email-code">Enter confirmation code</h2>
        <p class="auth-dialog__lead">We sent a 6-digit code to <strong data-email-code-target></strong>.</p>
        <form class="auth-form" data-email-code-form novalidate>
          <div class="auth-otp" role="group" aria-label="6-digit confirmation code">${boxes}</div>
          <p class="auth-error" data-auth-error aria-live="polite"></p>
          <button type="submit" class="auth-submit">Confirm email</button>
          <button type="button" class="auth-submit auth-submit--secondary" data-email-code-resend>Resend code</button>
        </form>
        <p class="auth-switch"><button type="button" data-settings-email-cancel>Cancel change</button></p>
      </div>
    </div>`;
}

function buildEmailResultDialog() {
  return `
    <div class="auth-overlay auth-overlay--stack" id="auth-overlay-email-result" data-auth-overlay="email-result" hidden>
      <div class="auth-dialog" role="dialog" aria-modal="true" aria-labelledby="auth-title-email-result">
        <button type="button" class="auth-dialog__close" data-email-change-close aria-label="Close">${closeIcon()}</button>
        <h2 class="auth-dialog__title" id="auth-title-email-result">Email change cancelled</h2>
        <p class="auth-dialog__lead" data-email-result-lead></p>
        <button type="button" class="auth-submit" data-email-change-close>OK</button>
      </div>
    </div>`;
}

async function onProfileSubmit(overlay, event) {
  event.preventDefault();

  const form = event.currentTarget;
  const usernameInput = overlay.querySelector('#settings-username');
  const emailInput = overlay.querySelector('#settings-email');
  const submitBtn = form.querySelector('button[type="submit"]');
  const username = usernameInput?.value.trim() || '';
  const email = emailInput?.value.trim() || '';
  const policy = emailPolicy(settingsState().draft);
  const currentEmail = policy.current;
  const emailChanged = email && email.toLowerCase() !== currentEmail.toLowerCase();

  if (!/^[A-Za-z0-9_]{3,32}$/.test(username)) {
    setStatus('profile:form', 'Username must be 3–32 characters: letters, numbers, underscore.', true);
    usernameInput?.focus();
    return;
  }

  if (emailChanged) {
    if (!policy.canEdit) {
      setStatus('profile:form', policy.managedByGoogle ? 'This email is managed by Google.' : 'This email can’t be changed from NumberLink.', true);
      return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setStatus('profile:form', 'Enter a valid email address.', true);
      emailInput?.focus();
      return;
    }
  } else if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    setStatus('profile:form', 'Enter a valid email address.', true);
    emailInput?.focus();
    return;
  }

  if (submitBtn) {
    submitBtn.disabled = true;
    submitBtn.setAttribute('aria-busy', 'true');
  }

  setStatus('profile:form', 'Saving…');

  try {
    const profile = await updateProfile({ username, email: currentEmail || undefined });
    settingsState().draft.username = profile.username ?? username;
    if (usernameInput) usernameInput.value = settingsState().draft.username;
    persistDraftAndNotify();
    if (!settingsState().draft.avatarUrl) refreshSettingsAvatar(overlay);

    if (!emailChanged) {
      setStatus('profile:form', 'Changes saved.');
      return;
    }

    const existingEmail = currentEmail || String(settingsState().user?.email || '').trim();
    if (existingEmail) {
      setStatus('profile:form', 'Username saved. Confirm your password to change email.');
      openEmailPasswordModal(email.toLowerCase());
      return;
    }

    const data = await startEmailChange({ email: email.toLowerCase() });
    applyEmailChangeFlags(settingsState().draft, data);
    remountContent();
    showSection('profile');
    openEmailCodeModal(data.pendingEmail || email);
    setStatus('profile:form', 'Check your inbox for a confirmation code.');
  } catch (err) {
    setStatus('profile:form', err.message || 'Could not update profile.', true);
  } finally {
    if (submitBtn) {
      submitBtn.disabled = false;
      submitBtn.removeAttribute('aria-busy');
    }
  }
}

async function onProviderLinkClick(btn) {
  const provider = btn.getAttribute('data-settings-link');
  if (!oauthProviders()[provider]) return;

  btn.disabled = true;
  setStatus('preferences:oauth', `Redirecting to ${providerLabel(provider)}…`);
  sessionStorage.setItem(oauthLinkKey(), provider);

  try {
    await prepareOauthLink(provider);
    window.location.assign(oauthAuthorizationUrl(provider));
  } catch (err) {
    sessionStorage.removeItem(oauthLinkKey());
    btn.disabled = false;
    setStatus('preferences:oauth', err.message || 'Could not connect that account.', true);
  }
}

async function onProviderUnlinkClick(btn) {
  const provider = btn.getAttribute('data-settings-unlink');
  if (!oauthProviders()[provider]) return;

  btn.disabled = true;
  setStatus('preferences:oauth', `Disconnecting ${providerLabel(provider)}…`);

  try {
    await unlinkOauthAccount(provider);
    const fresh = await fetchCurrentUser();
    if (fresh) {
      settingsState().user = fresh;
      settingsState().draft = buildDraftFromUser(fresh);
    } else {
      settingsState().draft[provider === 'google' ? 'googleAccount' : 'githubAccount'] = null;
    }
    remountContent();
    showSection('preferences');
    setStatus('preferences:oauth', `${providerLabel(provider)} disconnected.`);
  } catch (err) {
    btn.disabled = false;
    setStatus('preferences:oauth', err.message || 'Could not disconnect that account.', true);
  }
}

async function changeAccountPassword(currentPassword, newPassword) {
  const response = await fetch(`${backendApiUrl()}/me/password`, {
    method: 'PUT',
    credentials: 'include',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({ currentPassword, newPassword }),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not update password.');
  }
  return data;
}

async function onPasswordSubmit(event) {
  event.preventDefault();

  const form = event.currentTarget;
  const current = form.querySelector('[name="current"]')?.value ?? '';
  const next = form.querySelector('[name="newPassword"]')?.value ?? '';
  const confirm = form.querySelector('[name="confirm"]')?.value ?? '';

  if (!current || !next || !confirm) {
    setStatus('security:password', 'Fill in all password fields.', true);
    return;
  }

  if (!isStrongPassword(next)) {
    setStatus('security:password', 'Password needs 8+ characters with upper, lower, a number, and a symbol.', true);
    return;
  }

  if (next !== confirm) {
    setStatus('security:password', 'New passwords do not match.', true);
    return;
  }

  const btn = form.querySelector('[type="submit"]');
  if (btn) btn.disabled = true;

  try {
    await changeAccountPassword(current, next);
    form.reset();
    setStatus('security:password', 'Password updated.');
  } catch (err) {
    setStatus('security:password', err.message || 'Could not update password.', true);
  } finally {
    if (btn) btn.disabled = false;
  }
}

function onSessionRevoke(event) {
  const btn = event.target.closest('[data-settings-revoke]');
  if (!btn) return;

  const id = btn.getAttribute('data-settings-revoke');
  const isCurrent = btn.hasAttribute('data-settings-revoke-current');
  btn.disabled = true;
  revokeUserSession(id)
    .then(async (sessions) => {
      if (isCurrent) {
        await closeAccountSettings();
        const authRoot = document.querySelector('.header_auth');
        setDocumentAuthState(null);
        if (authRoot) {
          await transitionAuthChrome(authRoot, () => renderGuestAuth(authRoot));
        }
        return;
      }
      settingsState().sessions = sessions;
      remountContent();
      showSection('security');
      setStatus('security:sessions', 'Device signed out.');
    })
    .catch((err) => {
      btn.disabled = false;
      setStatus('security:sessions', err.message || 'Could not sign out that device.', true);
    });
}

function onRevokeOtherSessions() {
  const overlay = document.getElementById('nl-settings-overlay');
  const btn = overlay?.querySelector('[data-settings-revoke-others]');
  if (btn) btn.disabled = true;
  revokeOtherUserSessions()
    .then((sessions) => {
      settingsState().sessions = sessions;
      remountContent();
      showSection('security');
      setStatus('security:sessions', 'Other devices signed out.');
    })
    .catch((err) => {
      if (btn) btn.disabled = false;
      setStatus('security:sessions', err.message || 'Could not sign out other devices.', true);
    });
}

function onRevokeAllSessions() {
  const overlay = document.getElementById('nl-settings-overlay');
  const btn = overlay?.querySelector('[data-settings-revoke-all]');
  if (btn) btn.disabled = true;
  revokeAllUserSessions()
    .then(async () => {
      await closeAccountSettings();
      const authRoot = document.querySelector('.header_auth');
      setDocumentAuthState(null);
      if (authRoot) {
        await transitionAuthChrome(authRoot, () => renderGuestAuth(authRoot));
      }
    })
    .catch((err) => {
      if (btn) btn.disabled = false;
      setStatus('security:sessions', err.message || 'Could not sign out all devices.', true);
    });
}

async function startTwoFactorSetup() {
  const response = await fetch(`${backendApiUrl()}/me/2fa/setup`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not start two-factor setup.');
  }
  return data;
}

async function cancelTwoFactorSetup() {
  const response = await fetch(`${backendApiUrl()}/me/2fa/setup`, {
    method: 'DELETE',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    throw new Error(data.message || 'Could not cancel two-factor setup.');
  }
}

async function confirmTwoFactorSetup(code) {
  const response = await fetch(`${backendApiUrl()}/me/2fa/confirm`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({ code }),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not verify that code.');
  }
  return data;
}

async function disableTwoFactor(code) {
  const response = await fetch(`${backendApiUrl()}/me/2fa/disable`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({ code }),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not turn off two-factor authentication.');
  }
  return data;
}

async function completeTwoFactorLogin(code) {
  const response = await fetch(`${backendApiUrl()}/login/2fa`, {
    method: 'POST',
    credentials: 'include',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({ code }),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'That code is incorrect or expired.');
  }
  return data;
}

function refreshTwoFactorPanel() {
  remountContent();
  showSection('security');
}

async function onTwoFactorToggle(overlay, event) {
  const on = event.target.checked;
  const draft = settingsState().draft;

  if (on) {
    settingsState().twoFactorDisabling = false;
    if (draft.twoFactorEnabled) {
      refreshTwoFactorPanel();
      return;
    }
    event.target.disabled = true;
    setStatus('security:2fa', 'Preparing authenticator setup…');
    try {
      settingsState().twoFactorSetup = await startTwoFactorSetup();
      refreshTwoFactorPanel();
      setStatus('security:2fa', 'Scan the QR code or enter the setup key, then verify.');
    } catch (err) {
      event.target.checked = false;
      event.target.disabled = false;
      setStatus('security:2fa', err.message || 'Could not start two-factor setup.', true);
    }
    return;
  }

  if (draft.twoFactorEnabled) {
    settingsState().twoFactorDisabling = true;
    refreshTwoFactorPanel();
    return;
  }

  try {
    await cancelTwoFactorSetup();
  } catch (_) {
    /* pending setup may already be gone */
  }
  settingsState().twoFactorSetup = null;
  settingsState().twoFactorDisabling = false;
  refreshTwoFactorPanel();
  setStatus('security:2fa', 'Two-factor setup cancelled.');
}

async function onTwoFactorConfirm(overlay) {
  const code = overlay.querySelector('#settings-2fa-code')?.value.replace(/\D/g, '') || '';
  if (!/^\d{6}$/.test(code)) {
    setStatus('security:2fa', 'Enter the 6-digit code from your authenticator.', true);
    return;
  }

  const btn = overlay.querySelector('[data-settings-2fa-confirm]');
  if (btn) btn.disabled = true;

  try {
    if (settingsState().draft.twoFactorEnabled && settingsState().twoFactorDisabling) {
      await disableTwoFactor(code);
      settingsState().draft.twoFactorEnabled = false;
      settingsState().twoFactorSetup = null;
      settingsState().twoFactorDisabling = false;
      if (settingsState().user) settingsState().user.twoFactorEnabled = false;
      refreshTwoFactorPanel();
      setStatus('security:2fa', 'Two-factor authentication turned off.');
      return;
    }

    await confirmTwoFactorSetup(code);
    settingsState().draft.twoFactorEnabled = true;
    settingsState().twoFactorSetup = null;
    settingsState().twoFactorDisabling = false;
    if (settingsState().user) settingsState().user.twoFactorEnabled = true;
    refreshTwoFactorPanel();
    setStatus('security:2fa', 'Two-factor authentication is on.');
  } catch (err) {
    if (btn) btn.disabled = false;
    setStatus('security:2fa', err.message || 'Could not verify that code.', true);
  }
}

async function onTwoFactorCopy() {
  const secret = settingsState().twoFactorSetup?.secret || '';
  const compact = secret.replace(/\s+/g, '');
  if (!compact) return;
  try {
    await navigator.clipboard.writeText(compact);
    setStatus('security:2fa', 'Setup key copied.');
  } catch (_) {
    setStatus('security:2fa', 'Could not copy the setup key.', true);
  }
}

function buildTwoFactorLoginDialog() {
  const boxes = Array.from({ length: 6 }, (_, i) => (
    `<input data-otp-input inputmode="numeric" autocomplete="${i === 0 ? 'one-time-code' : 'off'}" maxlength="1" aria-label="Digit ${i + 1}" />`
  )).join('');
  return `
    <div class="auth-overlay" id="auth-overlay-totp" data-auth-overlay="totp" hidden>
      <div class="auth-dialog" role="dialog" aria-modal="true" aria-labelledby="auth-title-totp">
        <button type="button" class="auth-dialog__close" data-auth-close aria-label="Close">${closeIcon()}</button>
        <h2 class="auth-dialog__title" id="auth-title-totp">Authenticator code</h2>
        <p class="auth-dialog__lead">Enter the 6-digit code from your authenticator app to finish signing in.</p>
        <form class="auth-form" data-totp-form novalidate>
          <div class="auth-otp" role="group" aria-label="6-digit authenticator code">${boxes}</div>
          <p class="auth-error" data-auth-error aria-live="polite"></p>
          <button type="submit" class="auth-submit">Verify</button>
        </form>
      </div>
    </div>`;
}

function openTwoFactorLogin() {
  resetAuthOverlays();
  const overlay = document.getElementById('auth-overlay-totp');
  if (!overlay) return;
  overlay.hidden = false;
  overlay.classList.add('is-open');
  document.body.style.overflow = 'hidden';
  overlay.querySelectorAll('[data-otp-input]').forEach((input) => { input.value = ''; });
  const err = overlay.querySelector('[data-auth-error]');
  if (err) err.textContent = '';
  bindOtpInputs(overlay);
  requestAnimationFrame(() => overlay.querySelector('[data-otp-input]')?.focus());
}

async function onTwoFactorLoginSubmit(event) {
  event.preventDefault();
  const overlay = event.currentTarget.closest('[data-auth-overlay]');
  const code = readOtpValue(overlay);
  const err = overlay?.querySelector('[data-auth-error]');
  const submitBtn = overlay?.querySelector('.auth-submit');
  if (!/^\d{6}$/.test(code)) {
    if (err) err.textContent = 'Enter the 6-digit code from your authenticator.';
    return;
  }
  if (err) err.textContent = '';
  setAuthSubmitBusy(submitBtn, true, { idle: 'Verify', busy: 'Verifying…' });
  try {
    const user = await completeTwoFactorLogin(code);
    await closeAuthAnimated();
    const auth = document.querySelector('.header_auth');
    if (auth && user) {
      setDocumentAuthState(user);
      await transitionAuthChrome(auth, () => renderUserAuth(auth, user));
    } else if (auth) {
      await refreshAuthHeader(auth);
    }
    const returnTo = consumeOAuthReturnPath();
    if (returnTo) window.location.assign(returnTo);
  } catch (e) {
    if (err) err.textContent = e.message || 'That code is incorrect or expired.';
    setAuthSubmitBusy(submitBtn, false, { idle: 'Verify', busy: 'Verifying…' });
  }
}

function syncPlayPageSoundButton(soundOn) {
  const muteButton = document.getElementById('sound_button');
  if (!muteButton) return;
  if (soundOn) {
    muteButton.innerHTML = `Sound<span class="d-flex align-items-center ps-1"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-volume-down" viewBox="0 0 16 16"><path d="M9 4a.5.5 0 0 0-.812-.39L5.825 5.5H3.5A.5.5 0 0 0 3 6v4a.5.5 0 0 0 .5.5h2.325l2.363 1.89A.5.5 0 0 0 9 12zm3.025 4a4.5 4.5 0 0 1-1.318 3.182l-.708-.708A3.5 3.5 0 0 0 11.025 8a3.5 3.5 0 0 0-1.026-2.474l.708-.708A4.5 4.5 0 0 1 12.025 8"/></svg></span>`;
  } else {
    muteButton.innerHTML = `Sound off<span class="d-flex align-items-center ps-1"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-volume-mute" viewBox="0 0 16 16"><path d="M6.717 3.55A.5.5 0 0 1 7 4v8a.5.5 0 0 1-.812.39L3.825 10.5H1.5A.5.5 0 0 1 1 10V6a.5.5 0 0 1 .5-.5h2.325l2.363-1.89a.5.5 0 0 1 .529-.06M6 5.04 4.312 6.39A.5.5 0 0 1 4 6.5H2v3h2a.5.5 0 0 1 .312.11L6 10.96zm7.854.606a.5.5 0 0 1 0 .708L12.207 8l1.647 1.646a.5.5 0 0 1-.708.708L11.5 8.707l-1.646 1.647a.5.5 0 0 1-.708-.708L10.793 8 9.146 6.354a.5.5 0 1 1 .708-.708L11.5 7.293l1.646-1.647a.5.5 0 0 1 .708 0"/></svg></span>`;
  }
}

function onSoundPrefToggle(overlay, event) {
  const on = event.target.checked;
  settingsState().draft.soundOn = on;
  localStorage.setItem('sound', on ? 'play' : 'muted');
  writeDraft(settingsState().draft);
  syncPlayPageSoundButton(on);

  const copy = overlay.querySelector('[data-settings-sound]')?.closest('.nl-settings__switch-row')?.querySelector('.nl-settings__switch-copy span');
  if (copy) copy.textContent = on ? 'On' : 'Off';
  setStatus('preferences:sound', on ? 'Sound effects on.' : 'Sound effects off.');
}

function onBoardSizeChange(overlay) {
  const width = Number(overlay.querySelector('[data-settings-board-width]')?.value || 7);
  const height = Number(overlay.querySelector('[data-settings-board-height]')?.value || 7);
  settingsState().draft.defaultWidth = width;
  settingsState().draft.defaultHeight = height;
  localStorage.setItem('field_width', String(width));
  localStorage.setItem('field_height', String(height));
  writeDraft(settingsState().draft);

  const widthEl = document.getElementById('width');
  const heightEl = document.getElementById('height');
  if (widthEl) widthEl.value = String(width);
  if (heightEl) heightEl.value = String(height);

  setStatus('preferences:board', `Default board set to ${width}×${height}.`);
}

function onNotifyToggle(event) {
  const key = event.target.getAttribute('data-settings-notify');
  if (!key) return;
  settingsState().draft[key] = event.target.checked;
  writeDraft(settingsState().draft);
  setStatus('notifications:email', 'Notification preferences saved.');
}

function bindOverlay(overlay) {
  if (!overlay || overlay.dataset.bound === '1') return;
  overlay.dataset.bound = '1';

  overlay.querySelectorAll('[data-settings-close]').forEach((btn) => {
    btn.addEventListener('click', () => closeAccountSettings());
  });

  overlay.querySelectorAll('[data-settings-nav]').forEach((btn) => {
    btn.addEventListener('click', () => showSection(btn.getAttribute('data-settings-nav')));
  });

  overlay.querySelector('[data-settings-avatar-pick]')?.addEventListener('click', () => {
    overlay.querySelector('#settings-avatar-file')?.click();
  });

  overlay.querySelector('#settings-avatar-file')?.addEventListener('change', (e) => {
    onAvatarFileChange(overlay, e);
  });

  overlay.querySelector('[data-settings-avatar-clear]')?.addEventListener('click', () => {
    onAvatarClear(overlay);
  });

  overlay.querySelector('[data-settings-form="profile"]')?.addEventListener('submit', (e) => {
    onProfileSubmit(overlay, e);
  });

  overlay.querySelector('[data-settings-email-code]')?.addEventListener('click', () => {
    openEmailCodeModal(settingsState().draft?.pendingEmail || '');
  });

  overlay.querySelector('[data-settings-email-cancel]')?.addEventListener('click', () => {
    onSettingsCancelEmailChange();
  });

  overlay.querySelectorAll('[data-settings-link]').forEach((btn) => {
    btn.addEventListener('click', () => onProviderLinkClick(btn));
  });

  overlay.querySelectorAll('[data-settings-unlink]').forEach((btn) => {
    btn.addEventListener('click', () => onProviderUnlinkClick(btn));
  });

  overlay.querySelector('[data-settings-form="password"]')?.addEventListener('submit', onPasswordSubmit);

  overlay.querySelector('[data-settings-sessions]')?.addEventListener('click', onSessionRevoke);

  overlay.querySelector('[data-settings-revoke-others]')?.addEventListener('click', onRevokeOtherSessions);

  overlay.querySelector('[data-settings-revoke-all]')?.addEventListener('click', onRevokeAllSessions);

  overlay.querySelector('[data-settings-2fa]')?.addEventListener('change', (e) => {
    onTwoFactorToggle(overlay, e);
  });

  overlay.querySelector('[data-settings-2fa-confirm]')?.addEventListener('click', () => {
    onTwoFactorConfirm(overlay);
  });

  overlay.querySelector('[data-settings-2fa-copy]')?.addEventListener('click', () => {
    onTwoFactorCopy();
  });

  overlay.querySelector('[data-settings-sound]')?.addEventListener('change', (e) => {
    onSoundPrefToggle(overlay, e);
  });

  overlay.querySelector('[data-settings-board-width]')?.addEventListener('change', () => {
    onBoardSizeChange(overlay);
  });

  overlay.querySelector('[data-settings-board-height]')?.addEventListener('change', () => {
    onBoardSizeChange(overlay);
  });

  overlay.querySelectorAll('[data-settings-notify]').forEach((input) => {
    input.addEventListener('change', onNotifyToggle);
  });
}

async function openAccountSettings(user, options = {}) {
  const fresh = await fetchCurrentUser();
  const account = fresh || user;
  if (!account) return;
  settingsState().user = account;
  settingsState().onLocalProfileChange = options.onLocalProfileChange || null;
  settingsState().draft = buildDraftFromUser(account);
  settingsState().section = options.section === 'signin'
    ? 'preferences'
    : (options.section || 'profile');
  settingsState().sessions = await fetchUserSessions();

  ensureMounted();
  remountContent();

  const overlay = document.getElementById('nl-settings-overlay');
  if (!overlay) return;

  overlay.hidden = false;
  overlay.classList.remove('is-closing');
  overlay.classList.add('is-open');
  document.body.style.overflow = 'hidden';
  showSection(settingsState().section);

  const dialog = overlay.querySelector('.nl-settings');
  requestAnimationFrame(() => dialog?.focus({ preventScroll: true }));
}

async function closeAccountSettings() {
  const overlay = document.getElementById('nl-settings-overlay');
  if (!overlay || overlay.hidden) return;

  if (!prefersReducedMotion()) {
    overlay.classList.add('is-closing');
    overlay.classList.remove('is-open');
    await wait(140);
  }

  overlay.classList.remove('is-open', 'is-closing');
  overlay.hidden = true;
  if (!document.querySelector('.auth-overlay.is-open')) {
    document.body.style.overflow = '';
  }
}

function getSettingsGearButtonHtml() {
  return `
    <button
      type="button"
      class="auth-menu__settings"
      role="menuitem"
      data-auth-settings
      aria-label="Account settings"
      title="Account settings"
    >
      ${gearIcon()}
    </button>`;
}

/** Escape closes settings when open (auth-ui wires Escape for menu too). */
function isSettingsOpen() {
  const overlay = document.getElementById('nl-settings-overlay');
  return Boolean(overlay?.classList.contains('is-open'));
}

/* ——— auth ——— */

// Shared header auth actions + login/signup dialogs (all pages).


/** Spring Security OAuth2 authorization entrypoints */
function oauthProviders() {
  return {
  google: 'google',
  github: 'github',
};
}

function oauthPendingKey() {
  return 'numberlink.oauth.pending';
}
function oauthReturnKey() {
  return 'numberlink.oauth.returnTo';
}
function oauthLinkKey() {
  return 'numberlink.oauth.link';
}




function chevronIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" viewBox="0 0 16 16" aria-hidden="true"><path d="M4 6l4 4 4-4"/></svg>`;
}

function logoutIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" viewBox="0 0 16 16" aria-hidden="true"><path d="M6 14H3.5A1.5 1.5 0 0 1 2 12.5v-9A1.5 1.5 0 0 1 3.5 2H6"/><path d="M10.5 11.5 14 8l-3.5-3.5"/><path d="M14 8H6"/></svg>`;
}

function suggestIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" viewBox="0 0 16 16" aria-hidden="true"><path d="M2.5 3.5A5.5 5.5 0 0 1 12.4 6"/><path d="M13.5 12.5A5.5 5.5 0 0 1 3.6 10"/><path d="M12.5 2.5v3.5H9"/><path d="M3.5 13.5v-3.5H7"/></svg>`;
}

function mailIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="none" stroke="currentColor" stroke-width="1.75" stroke-linecap="round" stroke-linejoin="round" viewBox="0 0 24 24" aria-hidden="true"><rect x="3" y="5" width="18" height="14" rx="3"/><path d="m3.5 8.5 7.6 4.9a1.6 1.6 0 0 0 1.8 0l7.6-4.9"/></svg>`;
}

function buildVerifyEmailDialog() {
  return `
    <div class="auth-overlay" id="auth-overlay-verify" data-auth-overlay="verify" hidden>
      <div
        class="auth-dialog auth-dialog--verify"
        role="dialog"
        aria-modal="true"
        aria-labelledby="auth-title-verify"
        aria-describedby="auth-desc-verify"
      >
        <button type="button" class="auth-dialog__close" data-auth-close aria-label="Close">${closeIcon()}</button>
        <div class="auth-verify__icon" aria-hidden="true">${mailIcon()}</div>
        <h2 class="auth-dialog__title" id="auth-title-verify">Check your email</h2>
        <p class="auth-dialog__lead auth-verify__lead" id="auth-desc-verify">We sent a confirmation link to <strong data-verify-email></strong>. Open it to activate your account — we’ll sign you in here automatically.</p>
        <p class="auth-error" data-auth-error aria-live="polite"></p>
        <button type="button" class="auth-submit auth-submit--secondary" data-auth-resend>Resend email</button>
        <p class="auth-status" data-auth-status aria-live="polite"></p>
        <p class="auth-switch">Already confirmed?&ensp;<button type="button" data-auth-switch="login">Log in</button></p>
        <p class="auth-switch">Wrong email?&ensp;<button type="button" data-auth-switch="signup">Sign up again</button></p>
      </div>
    </div>`;
}

function buildForgotPasswordDialog() {
  return `
    <div class="auth-overlay" id="auth-overlay-forgot" data-auth-overlay="forgot" hidden>
      <div class="auth-dialog" role="dialog" aria-modal="true" aria-labelledby="auth-title-forgot">
        <button type="button" class="auth-dialog__close" data-auth-close aria-label="Close">${closeIcon()}</button>
        <h2 class="auth-dialog__title" id="auth-title-forgot">Reset password</h2>
        <p class="auth-dialog__lead">Enter the email for your NumberLink account. We’ll send a reset link if it matches a password login.</p>
        <form class="auth-form" data-auth-form="forgot" novalidate>
          <div class="auth-field">
            <label for="auth-forgot-email">Email</label>
            <input id="auth-forgot-email" name="email" type="email" autocomplete="email" placeholder="you@example.com" required />
          </div>
          <p class="auth-error" data-auth-error aria-live="polite"></p>
          <button type="submit" class="auth-submit">Send reset link</button>
        </form>
        <p class="auth-status" data-auth-status aria-live="polite"></p>
        <p class="auth-switch">Remembered it?&ensp;<button type="button" data-auth-switch="login">Log in</button></p>
      </div>
    </div>`;
}

function buildResetPasswordDialog() {
  return `
    <div class="auth-overlay" id="auth-overlay-reset" data-auth-overlay="reset" hidden>
      <div class="auth-dialog" role="dialog" aria-modal="true" aria-labelledby="auth-title-reset">
        <button type="button" class="auth-dialog__close" data-auth-close aria-label="Close">${closeIcon()}</button>
        <h2 class="auth-dialog__title" id="auth-title-reset">Choose a new password</h2>
        <p class="auth-dialog__lead">Pick a new password for your account. You’ll need to log in afterward.</p>
        <form class="auth-form" data-auth-form="reset" novalidate>
          <input type="hidden" name="token" data-reset-token value="" />
          <div class="auth-field">
            <label for="auth-reset-password">New password</label>
            <input id="auth-reset-password" name="password" type="password" autocomplete="new-password" placeholder="••••••••" required minlength="8" />
          </div>
          <div class="auth-field">
            <label for="auth-reset-confirm">Confirm password</label>
            <input id="auth-reset-confirm" name="confirm" type="password" autocomplete="new-password" placeholder="Repeat password" required minlength="8" />
          </div>
          <p class="auth-error" data-auth-error aria-live="polite"></p>
          <button type="submit" class="auth-submit">Update password</button>
        </form>
        <p class="auth-status" data-auth-status aria-live="polite"></p>
        <p class="auth-switch">Back to&ensp;<button type="button" data-auth-switch="login">Log in</button></p>
      </div>
    </div>`;
}

function buildDialog(mode) {
    const isLogin = mode === 'login';
    const title = isLogin ? 'Log in' : 'Sign up';
    const lead = isLogin
        ? 'Welcome back. Enter your details to continue.'
        : 'Create an account to save scores and reviews.';
    const submit = isLogin ? 'Log in' : 'Create account';

    const usernameField = isLogin ? '' : `
      <div class="auth-field">
        <div class="auth-field__head">
          <label for="auth-${mode}-username">Username</label>
          <button type="button" class="auth-suggest" data-auth-suggest-username aria-label="Suggest a username">
            ${suggestIcon()}<span>Suggest</span>
          </button>
        </div>
        <input id="auth-${mode}-username" name="username" type="text" autocomplete="username" placeholder="Your name" required minlength="3" maxlength="32" pattern="[A-Za-z0-9_]{3,32}" />
      </div>`;

    const confirmField = isLogin ? '' : `
      <div class="auth-field">
        <label for="auth-${mode}-confirm">Confirm password</label>
        <input id="auth-${mode}-confirm" name="confirm" type="password" autocomplete="new-password" placeholder="Repeat password" required />
      </div>`;

    const switchHtml = isLogin
        ? `Don’t have an account? <button type="button" data-auth-switch="signup">Sign up</button>`
        : `Already have an account? <button type="button" data-auth-switch="login">Log in</button>`;

    return `
    <div class="auth-overlay" id="auth-overlay-${mode}" data-auth-overlay="${mode}" hidden>
      <div class="auth-dialog" role="dialog" aria-modal="true" aria-labelledby="auth-title-${mode}">
        <button type="button" class="auth-dialog__close" data-auth-close aria-label="Close">${closeIcon()}</button>
        <h2 class="auth-dialog__title" id="auth-title-${mode}">${title}</h2>
        <p class="auth-dialog__lead">${lead}</p>
        <div class="auth-oauth" role="group" aria-label="Continue with a provider">
          <button type="button" class="auth-oauth__btn" data-oauth="google">
            ${googleIcon()}<span>Google</span>
          </button>
          <button type="button" class="auth-oauth__btn auth-oauth__btn--github" data-oauth="github">
            ${githubIcon()}<span>GitHub</span>
          </button>
        </div>
        <div class="auth-divider" aria-hidden="true">or</div>
        <form class="auth-form" data-auth-form="${mode}" novalidate>
          ${usernameField}
          <div class="auth-field">
            <label for="auth-${mode}-email">${isLogin ? 'Email or username' : 'Email'}</label>
            <input
              id="auth-${mode}-email"
              name="${isLogin ? 'login' : 'email'}"
              type="${isLogin ? 'text' : 'email'}"
              autocomplete="${isLogin ? 'username' : 'email'}"
              placeholder="${isLogin ? 'you@example.com or username' : 'you@example.com'}"
              required
            />
          </div>
          <div class="auth-field">
            <div class="auth-field__head">
              <label for="auth-${mode}-password">Password</label>
              ${isLogin ? `<button type="button" class="auth-forgot" data-auth-forgot>Forgot password?</button>` : ''}
            </div>
            <input id="auth-${mode}-password" name="password" type="password" autocomplete="${isLogin ? 'current-password' : 'new-password'}" placeholder="••••••••" required minlength="${isLogin ? 1 : 8}" />
          </div>
          ${confirmField}
          <p class="auth-error" data-auth-error aria-live="polite"></p>
          <button type="submit" class="auth-submit">${submit}</button>
        </form>
        <p class="auth-status" data-auth-status></p>
        <p class="auth-switch">${switchHtml}</p>
      </div>
    </div>`;
}

function openAuth(mode) {
    resetAuthOverlays();
    const overlay = document.getElementById(`auth-overlay-${mode}`);
    if (!overlay) return;

    overlay.hidden = false;
    overlay.classList.add('is-open');
    document.body.style.overflow = 'hidden';

    const first = overlay.querySelector('input:not([type="hidden"])');
    requestAnimationFrame(() => first?.focus());
}

function looksLikeEmail(value) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(String(value || '').trim());
}

function openForgotPassword(prefillEmail = '') {
  openAuth('forgot');
  const overlay = document.getElementById('auth-overlay-forgot');
  if (!overlay) return;
  const emailInput = overlay.querySelector('#auth-forgot-email');
  if (emailInput && looksLikeEmail(prefillEmail)) {
    emailInput.value = String(prefillEmail).trim();
  }
  clearAuthFormFeedback(overlay);
  requestAnimationFrame(() => {
    if (emailInput?.value) overlay.querySelector('.auth-submit')?.focus();
    else emailInput?.focus();
  });
}

function openResetPassword(token) {
  openAuth('reset');
  const overlay = document.getElementById('auth-overlay-reset');
  if (!overlay) return;
  const tokenInput = overlay.querySelector('[data-reset-token]');
  if (tokenInput) tokenInput.value = token || '';
  clearAuthFormFeedback(overlay);
  const form = overlay.querySelector('[data-auth-form="reset"]');
  form?.reset();
  if (tokenInput) tokenInput.value = token || '';
  requestAnimationFrame(() => overlay.querySelector('#auth-reset-password')?.focus());
}

function consumeResetTokenFromUrl() {
  const params = new URLSearchParams(window.location.search);
  const token = params.get('resetToken');
  if (!token) return null;
  params.delete('resetToken');
  const next = `${window.location.pathname}${params.toString() ? `?${params}` : ''}${window.location.hash}`;
  window.history.replaceState({}, '', next);
  return token;
}

const VERIFY_RESEND_COOLDOWN_MS = 60_000;
const VERIFY_RESEND_STORAGE_PREFIX = 'nl_verify_resend_at:';

function verifyResendStorageKey(email) {
    return `${VERIFY_RESEND_STORAGE_PREFIX}${String(email || '').trim().toLowerCase()}`;
}

function readVerifyResendUntil(email) {
    try {
        const raw = sessionStorage.getItem(verifyResendStorageKey(email));
        const until = Number(raw);
        return Number.isFinite(until) ? until : 0;
    } catch (_) {
        return 0;
    }
}

function writeVerifyResendUntil(email, untilMs) {
    try {
        sessionStorage.setItem(verifyResendStorageKey(email), String(untilMs));
    } catch (_) {}
}

function clearVerifyResendTimer(overlay) {
    if (!overlay?._verifyResendTimer) return;
    clearInterval(overlay._verifyResendTimer);
    overlay._verifyResendTimer = null;
}

const VERIFY_AUTH_POLL_MS = 2500;
let verifyAuthPollTimer = null;

function stopVerifyAuthPolling() {
    if (!verifyAuthPollTimer) return;
    clearInterval(verifyAuthPollTimer);
    verifyAuthPollTimer = null;
}

function isVerifyOverlayOpen() {
    const overlay = document.getElementById('auth-overlay-verify');
    return Boolean(overlay && !overlay.hidden && overlay.classList.contains('is-open'));
}

/**
 * After the user confirms in email, the verify endpoint should create a session.
 * This tab polls /api/me and auto-closes + signs in when that session appears.
 */
async function completeVerifyAuthIfReady() {
    if (!isVerifyOverlayOpen()) {
        stopVerifyAuthPolling();
        return false;
    }
    if (document.visibilityState === 'hidden') return false;

    const user = await fetchCurrentUser();
    if (!user) return false;

    stopVerifyAuthPolling();
    try {
        localStorage.setItem('nl_email_verified_ping', String(Date.now()));
    } catch (_) {}

    const auth = document.querySelector('.header_auth');
    await closeAuthAnimated();
    if (auth) {
        setDocumentAuthState(user);
        await transitionAuthChrome(auth, () => renderUserAuth(auth, user));
    }
    return true;
}

function startVerifyAuthPolling() {
    stopVerifyAuthPolling();
    verifyAuthPollTimer = setInterval(() => {
        completeVerifyAuthIfReady().catch(() => {});
    }, VERIFY_AUTH_POLL_MS);
    setTimeout(() => {
        completeVerifyAuthIfReady().catch(() => {});
    }, 600);
}

function bindVerifyAuthCrossTab() {
    if (window.__nlVerifyAuthBound) return;
    window.__nlVerifyAuthBound = true;

    window.addEventListener('storage', (e) => {
        if (e.key !== 'nl_email_verified_ping' || !isVerifyOverlayOpen()) return;
        completeVerifyAuthIfReady().catch(() => {});
    });

    document.addEventListener('visibilitychange', () => {
        if (document.visibilityState === 'visible' && isVerifyOverlayOpen()) {
            completeVerifyAuthIfReady().catch(() => {});
        }
    });
}

function formatResendCountdown(remainingMs) {
    const seconds = Math.max(0, Math.ceil(remainingMs / 1000));
    return `Resend in ${seconds}s`;
}

function setVerifyResendIdle(overlay) {
    const btn = overlay?.querySelector('[data-auth-resend]');
    if (!btn) return;
    btn.disabled = false;
    btn.classList.remove('is-busy', 'is-cooling');
    btn.textContent = 'Resend email';
    btn.removeAttribute('aria-disabled');
}

function tickVerifyResendCooldown(overlay) {
    const btn = overlay.querySelector('[data-auth-resend]');
    if (!btn) return;

    const until = Number(overlay.dataset.resendUntil || 0);
    const remaining = until - Date.now();
    if (remaining <= 0) {
        clearVerifyResendTimer(overlay);
        delete overlay.dataset.resendUntil;
        setVerifyResendIdle(overlay);
        return;
    }

    btn.disabled = true;
    btn.classList.add('is-cooling');
    btn.classList.remove('is-busy');
    btn.setAttribute('aria-disabled', 'true');
    btn.textContent = formatResendCountdown(remaining);
}

/** Disable resend and show a live countdown (production auth pattern: 30–60s). */
function startVerifyResendCooldown(overlay, { durationMs = VERIFY_RESEND_COOLDOWN_MS } = {}) {
    if (!overlay) return;
    const email = overlay.dataset.verifyEmail || '';
    const until = Date.now() + durationMs;
    overlay.dataset.resendUntil = String(until);
    if (email) writeVerifyResendUntil(email, until);

    clearVerifyResendTimer(overlay);
    tickVerifyResendCooldown(overlay);
    overlay._verifyResendTimer = setInterval(() => tickVerifyResendCooldown(overlay), 250);
}

function resumeVerifyResendCooldown(overlay) {
    const email = overlay?.dataset.verifyEmail || '';
    const storedUntil = email ? readVerifyResendUntil(email) : 0;
    const remaining = storedUntil - Date.now();
    if (remaining <= 0) {
        setVerifyResendIdle(overlay);
        return;
    }
    overlay.dataset.resendUntil = String(storedUntil);
    clearVerifyResendTimer(overlay);
    tickVerifyResendCooldown(overlay);
    overlay._verifyResendTimer = setInterval(() => tickVerifyResendCooldown(overlay), 250);
}

const AUTH_SUBMIT_IDLE = {
    login: 'Log in',
    signup: 'Create account',
    forgot: 'Send reset link',
    reset: 'Update password',
    confirmed: 'Continue',
    'confirm-failed': 'Log in',
};

function resetAuthOverlays() {
    document.querySelectorAll('[data-auth-overlay]').forEach((overlay) => {
        overlay.classList.remove('is-open', 'is-closing');
        overlay.hidden = true;
        const form = overlay.querySelector('form');
        const err = overlay.querySelector('[data-auth-error]');
        const status = overlay.querySelector('[data-auth-status]');
        const submitBtn = overlay.querySelector('.auth-submit');
        form?.reset();
        if (err) err.textContent = '';
        if (status) {
            status.textContent = '';
            status.classList.remove('is-visible');
        }
        if (overlay.getAttribute('data-auth-overlay') === 'verify') {
            clearVerifyResendTimer(overlay);
            stopVerifyAuthPolling();
            delete overlay.dataset.verifyEmail;
            delete overlay.dataset.resendUntil;
            const emailEl = overlay.querySelector('[data-verify-email]');
            if (emailEl) emailEl.textContent = '';
            setVerifyResendIdle(overlay);
        } else if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.classList.remove('is-busy');
            const mode = overlay.getAttribute('data-auth-overlay');
            const idleLabel = AUTH_SUBMIT_IDLE[mode];
            if (idleLabel) submitBtn.textContent = idleLabel;
        }
        overlay.querySelectorAll('input.is-invalid').forEach((el) => el.classList.remove('is-invalid'));
    });
    document.body.style.overflow = '';
}

/**
 * @param {string} email
 * @param {{ sentJustNow?: boolean }} [options] — when true (post-signup), start cooldown immediately
 */
function openVerifyEmail(email, { sentJustNow = false } = {}) {
    resetAuthOverlays();
    const overlay = document.getElementById('auth-overlay-verify');
    if (!overlay) return;

    const normalized = String(email || '').trim();
    overlay.dataset.verifyEmail = normalized;
    const emailEl = overlay.querySelector('[data-verify-email]');
    if (emailEl) emailEl.textContent = normalized || 'your inbox';

    overlay.hidden = false;
    overlay.classList.add('is-open');
    document.body.style.overflow = 'hidden';

    if (sentJustNow) {
        startVerifyResendCooldown(overlay);
    } else {
        resumeVerifyResendCooldown(overlay);
    }

    bindVerifyAuthCrossTab();
    startVerifyAuthPolling();

    requestAnimationFrame(() => {
        const focusEl = overlay.querySelector('[data-auth-resend]:not(:disabled)')
            || overlay.querySelector('[data-auth-close]');
        focusEl?.focus();
    });
}

function isUnverifiedEmailError(err) {
    const code = String(err?.code || '').toUpperCase();
    const msg = String(err?.message || '').toLowerCase();
    return code === 'EMAIL_NOT_VERIFIED'
        || (msg.includes('verify') && msg.includes('email'))
        || msg.includes('not verified');
}

function closeAuth() {
    resetAuthOverlays();
}

/** Animated close after successful login/signup — rare action, short exit OK */
async function closeAuthAnimated() {
    const open = document.querySelector('.auth-overlay.is-open');
    if (!open || prefersReducedMotion()) {
        resetAuthOverlays();
        return;
    }

    open.classList.add('is-closing');
    open.classList.remove('is-open');
    await wait(140);
    resetAuthOverlays();
}

/** Guest ↔ user header swap after login/logout (not on cold page load) */
async function transitionAuthChrome(authRoot, renderFn) {
    if (!prefersReducedMotion()) {
        authRoot.classList.remove('is-entering');
        authRoot.classList.add('is-exiting');
        await wait(140);
        authRoot.classList.remove('is-exiting');
    }

    renderFn();

    if (!prefersReducedMotion()) {
        authRoot.classList.add('is-entering');
        await wait(180);
        authRoot.classList.remove('is-entering');
    }
}

function validateForm(form, mode) {
    const errorEl = form.querySelector('[data-auth-error]');
    const inputs = [...form.querySelectorAll('input')];
    inputs.forEach((el) => el.classList.remove('is-invalid'));
    errorEl.textContent = '';

    for (const input of inputs) {
        // Password may intentionally contain leading/trailing spaces — don't trim it for empty check
        const empty = input.name === 'password' || input.name === 'confirm'
            ? !input.value
            : !input.value.trim();

        if (empty) {
            input.classList.add('is-invalid');
            errorEl.textContent = 'Please fill in all fields.';
            input.focus();
            return false;
        }
        if (input.type === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(input.value.trim())) {
            input.classList.add('is-invalid');
            errorEl.textContent = 'Enter a valid email address.';
            input.focus();
            return false;
        }
    }

    if (mode === 'signup' || mode === 'reset') {
        const passwordInput = form.querySelector('[name="password"]');
        const confirmInput = form.querySelector('[name="confirm"]');
        const password = passwordInput?.value ?? '';
        const confirm = confirmInput?.value ?? '';

        if (!isStrongPassword(password)) {
            passwordInput?.classList.add('is-invalid');
            errorEl.textContent =
                'Password must be at least 8 characters and include uppercase, lowercase, a number, and a special character.';
            passwordInput?.focus();
            return false;
        }

        if (password !== confirm) {
            confirmInput?.classList.add('is-invalid');
            errorEl.textContent = 'Passwords do not match.';
            confirmInput?.focus();
            return false;
        }
    }

    return true;
}

async function validateAuthorization(mode) {
  switch (mode) {
    case 'signup':
      return await registerUser();
    case 'login':
      return await loginUser();
    default:
      return null;
  }
}

async function fetchSuggestedUsername() {
  const response = await fetch(`${backendApiUrl()}/generate-name`, {
    method: 'GET',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });

  if (!response.ok) {
    let message = 'Could not suggest a username.';
    try {
      const data = await response.json();
      message = data.message || data.error || message;
    } catch (_) {}
    throw new Error(message);
  }

  const data = await response.json();
  const username = typeof data?.username === 'string' ? data.username.trim() : '';
  if (!username) {
    throw new Error('Could not suggest a username.');
  }
  return username;
}

async function suggestSignupUsername(overlay) {
  const btn = overlay.querySelector('[data-auth-suggest-username]');
  const input = overlay.querySelector('#auth-signup-username');
  const errorEl = overlay.querySelector('[data-auth-error]');
  if (!btn || !input || btn.classList.contains('is-busy')) return;

  btn.classList.add('is-busy');
  btn.setAttribute('aria-busy', 'true');
  if (errorEl) errorEl.textContent = '';
  input.classList.remove('is-invalid');

  try {
    const username = await fetchSuggestedUsername();
    input.value = username;
    input.classList.remove('is-invalid');
    input.dispatchEvent(new Event('input', { bubbles: true }));
    if (!prefersReducedMotion()) {
      btn.classList.add('is-flash');
      await wait(450);
      btn.classList.remove('is-flash');
    }
    input.focus();
    input.select();
  } catch (err) {
    if (errorEl) errorEl.textContent = err.message || 'Could not suggest a username.';
  } finally {
    btn.classList.remove('is-busy');
    btn.removeAttribute('aria-busy');
  }
}

async function registerUser() {
    const username = document.getElementById('auth-signup-username').value.trim();
    const email = document.getElementById('auth-signup-email').value.trim();
    const password = document.getElementById('auth-signup-password').value;

    const response = await fetch(`${backendApiUrl()}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password }),
      credentials: 'include'
    });

    if (!response.ok) {
      let message = 'Unable to proceed registration request';
      try {
        const data = await response.json();
        message = data.message || data.error || message;
      } catch (_) {}
      throw new Error(message);
    }

    const data = await response.json().catch(() => ({}));
    return { ...data, email, verificationRequired: true };
}

async function loginUser() {
  const login = document.getElementById('auth-login-email').value.trim();
  const password = document.getElementById('auth-login-password').value.trim();

  const response = await fetch(`${backendApiUrl()}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ login, password }),
      credentials: 'include'
    });

    if (!response.ok) {
      let message = 'Unable to proceed login request';
      let code = '';
      let email = '';
      try {
        const data = await response.json();
        message = data.message || data.error || message;
        code = data.code || data.error || '';
        email = typeof data.email === 'string' ? data.email : '';
      } catch (_) {}
      const err = new Error(message);
      err.code = code;
      err.email = email;
      throw err;
    }

    return response.json();
}

async function resendVerificationEmail(email) {
  const normalized = String(email || '').trim();
  if (!normalized) {
    throw new Error('Missing email address.');
  }

  const response = await fetch(`${backendApiUrl()}/resend-verification`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify({ email: normalized }),
  });

  if (!response.ok) {
    let message = 'Could not resend the email.';
    try {
      const data = await response.json();
      message = data.message || data.error || message;
    } catch (_) {}
    throw new Error(message);
  }

  if (response.status === 204) return {};
  return response.json().catch(() => ({}));
}

async function requestPasswordReset(email) {
  const normalized = String(email || '').trim().toLowerCase();
  if (!normalized) {
    throw new Error('Enter your email address.');
  }

  const response = await fetch(`${backendApiUrl()}/forgot-password`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify({ email: normalized }),
  });

  if (!response.ok) {
    let message = 'Could not send the reset email.';
    try {
      const data = await response.json();
      message = data.message || data.error || message;
    } catch (_) {}
    throw new Error(message);
  }

  return response.json().catch(() => ({ ok: true }));
}

async function submitPasswordReset(token, password) {
  const response = await fetch(`${backendApiUrl()}/reset-password`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify({ token, password }),
  });

  if (!response.ok) {
    let message = 'Could not update password.';
    try {
      const data = await response.json();
      message = data.message || data.error || message;
    } catch (_) {}
    throw new Error(message);
  }

  return response.json().catch(() => ({ ok: true }));
}

function oauthAuthorizationUrl(provider) {
  const registrationId = oauthProviders()[provider];
  if (!registrationId) {
    throw new Error('Unsupported OAuth provider');
  }

  return `${backendOrigin()}/oauth2/authorization/${registrationId}`;
}

function currentAppPath() {
  return `${window.location.pathname}${window.location.search}${window.location.hash}`;
}

function rememberOAuthReturnPath() {
  const path = currentAppPath() || '/';
  sessionStorage.setItem(oauthReturnKey(), path);
}

function consumeOAuthReturnPath() {
  const target = sessionStorage.getItem(oauthReturnKey());
  sessionStorage.removeItem(oauthReturnKey());
  if (!target || !target.startsWith('/') || target.startsWith('//')) {
    return null;
  }
  return target === currentAppPath() ? null : target;
}

function markOAuthPending() {
  sessionStorage.setItem(oauthPendingKey(), '1');
}

function takeOAuthPending() {
  const pending = sessionStorage.getItem(oauthPendingKey()) === '1';
  sessionStorage.removeItem(oauthPendingKey());
  return pending;
}

function clearOAuthFlowState() {
  sessionStorage.removeItem(oauthPendingKey());
  sessionStorage.removeItem(oauthReturnKey());
}

function setOAuthButtonsBusy(busy) {
  document.querySelectorAll('[data-oauth]').forEach((btn) => {
    btn.disabled = busy;
    btn.classList.toggle('is-busy', busy);
    if (busy) {
      btn.dataset.label = btn.dataset.label || btn.textContent.trim();
      const label = btn.querySelector('span:last-child');
      if (label) label.textContent = 'Redirecting…';
    } else if (btn.dataset.label) {
      const label = btn.querySelector('span:last-child');
      if (label) label.textContent = btn.dataset.label;
    }
  });
}

async function prepareOauthLink(provider) {
  const response = await fetch(`${backendApiUrl()}/me/oauth/prepare-link`, {
    method: 'POST',
    credentials: 'include',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      provider,
      returnTo: `${window.location.pathname}${window.location.search}`,
    }),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not start account linking.');
  }
  return data;
}

async function unlinkOauthAccount(provider) {
  const response = await fetch(`${backendApiUrl()}/me/oauth/${encodeURIComponent(provider)}`, {
    method: 'DELETE',
    credentials: 'include',
    headers: { Accept: 'application/json' },
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Could not disconnect that account.');
  }
  return data;
}

function consumeOauthLinkReturn() {
  const params = new URLSearchParams(window.location.search);
  const state = params.get('oauthLink');
  const pending = sessionStorage.getItem(oauthLinkKey());
  if (state) {
    params.delete('oauthLink');
    const next = `${window.location.pathname}${params.toString() ? `?${params}` : ''}${window.location.hash}`;
    window.history.replaceState({}, '', next);
  }
  if (!state && !pending) return null;
  sessionStorage.removeItem(oauthLinkKey());
  return { state: state || 'cancelled' };
}

function finishOauthLinkReturn(auth, linkReturn) {
  refreshAuthHeader(auth).then(() => {
    if (!document.body.classList.contains('is-authenticated')) return;
    return openAccountSettings(null, { section: 'preferences' }).then(() => {
      const messages = {
        ok: 'Account connected.',
        taken: 'That account is already linked to another NumberLink user.',
        already: 'This sign-in method is already connected.',
        failed: 'Could not connect that account. Try again.',
        cancelled: 'Account linking was cancelled.',
      };
      const state = linkReturn.state || 'failed';
      setStatus('preferences:oauth', messages[state] || messages.failed, state !== 'ok');
    });
  });
}

function loginWithOAuth(provider) {
  const id = String(provider || '').toLowerCase();
  if (!oauthProviders()[id]) {
    throw new Error('Unsupported OAuth provider');
  }

  markOAuthPending();
  rememberOAuthReturnPath();
  setOAuthButtonsBusy(true);
  window.location.assign(oauthAuthorizationUrl(id));
}

/** Surface OAuth failure query (?login=failed) once, then clean the URL */
function consumeOAuthReturnParams() {
  const params = new URLSearchParams(window.location.search);
  const loginState = params.get('login');
  if (!loginState) return null;

  params.delete('login');
  const next = `${window.location.pathname}${params.toString() ? `?${params}` : ''}${window.location.hash}`;
  window.history.replaceState({}, '', next);

  if (loginState === '2fa') {
    return 'TWO_FACTOR';
  }
  if (loginState === 'failed') {
    clearOAuthFlowState();
    return 'OAuth sign-in failed. Try again or use email and password.';
  }
  return null;
}

async function fetchCurrentUser() {
  try {
    const response = await fetch(`${backendApiUrl()}/me`, {
      method: 'GET',
      credentials: 'include',
      headers: { Accept: 'application/json' },
    });

    if (response.status === 401 || response.status === 403 || !response.ok) {
      return null;
    }

    const contentType = response.headers.get('content-type') || '';
    if (!contentType.includes('application/json')) {
      return null;
    }

    return await response.json();
  } catch (_) {
    return null;
  }
}

async function logoutCurrentUser() {
  await fetch(`${backendApiUrl()}/logout`, {
    method: 'POST',
    credentials: 'include',
  });
}

function setDocumentAuthState(user) {
  document.body.classList.toggle('is-authenticated', Boolean(user));
  document.body.dataset.authUser = user?.username || '';
  document.body.dataset.authReady = '1';
  document.dispatchEvent(new CustomEvent('numberlink:auth', {
    detail: { user: user || null },
  }));
}

function renderGuestAuth(authRoot) {
  authRoot.dataset.authState = 'guest';
  authRoot.innerHTML = `
    <button type="button" class="auth-btn auth-btn--ghost" data-auth-open="login">Login</button>
    <button type="button" class="auth-btn auth-btn--solid" data-auth-open="signup">Sign up</button>
  `;

  authRoot.querySelectorAll('[data-auth-open]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const container = document.querySelector('.header_container');
      container?.classList.remove('menu-open');
      const toggle = container?.querySelector('.menu_toggle');
      if (toggle) {
        toggle.setAttribute('aria-expanded', 'false');
        toggle.setAttribute('aria-label', 'Open menu');
      }
      openAuth(btn.getAttribute('data-auth-open'));
    });
  });
}

function closeUserMenu(authRoot) {
  const trigger = authRoot.querySelector('[data-auth-user-trigger]');
  const menu = authRoot.querySelector('[data-auth-user-menu]');
  if (!trigger || !menu || menu.hidden) return;
  trigger.setAttribute('aria-expanded', 'false');
  menu.hidden = true;
  trigger.focus({ preventScroll: true });
}

function openUserMenu(authRoot) {
  const trigger = authRoot.querySelector('[data-auth-user-trigger]');
  const menu = authRoot.querySelector('[data-auth-user-menu]');
  if (!trigger || !menu) return;
  trigger.setAttribute('aria-expanded', 'true');
  menu.hidden = false;
  const firstItem = menu.querySelector('[role="menuitem"]');
  requestAnimationFrame(() => firstItem?.focus({ preventScroll: true }));
}

function renderAuthSkeleton(authRoot) {
  authRoot.dataset.authState = 'loading';
  authRoot.innerHTML = `
    <div class="auth-user-skel" aria-hidden="true">
      <span class="auth-user-skel__circle"></span>
      <span class="auth-user-skel__bar"></span>
    </div>
  `;
}

function applyLocalProfileToHeader(authRoot, profile) {
  if (!authRoot || !profile) return;
  const name = profile.username || 'Player';
  const email = profile.email ? String(profile.email).trim() : '';
  const initials = userInitials(name);

  const nameEls = authRoot.querySelectorAll('.auth-user__name, .auth-menu__name');
  nameEls.forEach((el) => {
    el.textContent = name;
  });

  let emailEl = authRoot.querySelector('.auth-menu__email');
  if (email) {
    if (!emailEl) {
      const id = authRoot.querySelector('.auth-menu__id');
      if (id) {
        emailEl = document.createElement('p');
        emailEl.className = 'auth-menu__email';
        id.appendChild(emailEl);
      }
    }
    if (emailEl) emailEl.textContent = email;
  } else {
    emailEl?.remove();
  }

  const avatarSrc = resolveMediaUrl(profile.avatarUrl);
  const avatarHtml = avatarSrc
    ? `<img src="${escapeHtml(avatarSrc)}" alt="" />`
    : null;

  authRoot.querySelectorAll('.auth-user__avatar, .auth-menu__avatar').forEach((el) => {
    if (avatarHtml) el.innerHTML = avatarHtml;
    else el.textContent = initials;
  });

  const trigger = authRoot.querySelector('[data-auth-user-trigger]');
  if (trigger) {
    trigger.setAttribute('aria-label', `${name}, account menu`);
    trigger.setAttribute('title', name);
  }

  document.body.dataset.authUser = name;
}

function buildUserMenuHtml(name, email, initials, avatarUrl) {
  const avatarSrc = resolveMediaUrl(avatarUrl);
  const avatarInner = avatarSrc
    ? `<img src="${escapeHtml(avatarSrc)}" alt="" />`
    : initials;
  return `
    <div class="auth-user-wrap">
      <button
        type="button"
        class="auth-user"
        data-auth-user-trigger
        aria-haspopup="menu"
        aria-expanded="false"
        aria-controls="auth-user-menu"
        aria-label="${name}, account menu"
        title="${name}"
      >
        <span class="auth-user__identity">
          <span class="auth-user__avatar" aria-hidden="true">${avatarInner}</span>
          <span class="auth-user__name">${name}</span>
        </span>
        <span class="auth-user__chevron" aria-hidden="true">${chevronIcon()}</span>
      </button>
      <div class="auth-menu" id="auth-user-menu" role="menu" data-auth-user-menu hidden>
        <div class="auth-menu__header">
          <span class="auth-menu__avatar" aria-hidden="true">${avatarInner}</span>
          <div class="auth-menu__id">
            <p class="auth-menu__name">${name}</p>
            ${email ? `<p class="auth-menu__email">${email}</p>` : ''}
          </div>
          ${getSettingsGearButtonHtml()}
        </div>
        <button type="button" class="auth-menu__logout" role="menuitem" data-auth-logout>
          <span class="auth-menu__logout-icon" aria-hidden="true">${logoutIcon()}</span>
          <span>Log out</span>
        </button>
      </div>
    </div>
  `;
}

function bindUserMenu(authRoot, user) {
  const trigger = authRoot.querySelector('[data-auth-user-trigger]');
  const menu = authRoot.querySelector('[data-auth-user-menu]');

  trigger?.addEventListener('click', (e) => {
    e.stopPropagation();
    const isOpen = trigger.getAttribute('aria-expanded') === 'true';
    if (isOpen) closeUserMenu(authRoot);
    else openUserMenu(authRoot);
  });

  trigger?.addEventListener('keydown', (e) => {
    if (e.key === 'ArrowDown' || e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      openUserMenu(authRoot);
    }
  });

  menu?.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      e.preventDefault();
      closeUserMenu(authRoot);
      return;
    }
    if (e.key === 'ArrowUp' || e.key === 'Home') {
      e.preventDefault();
      menu.querySelector('[role="menuitem"]')?.focus();
    }
  });

  authRoot.querySelector('[data-auth-settings]')?.addEventListener('click', () => {
    closeUserMenu(authRoot);
    openAccountSettings(user, {
      onLocalProfileChange: (profile) => applyLocalProfileToHeader(authRoot, profile),
    });
  });

  authRoot.querySelector('[data-auth-logout]')?.addEventListener('click', async () => {
    const logoutBtn = authRoot.querySelector('[data-auth-logout]');
    logoutBtn?.classList.add('is-busy');
    logoutBtn?.setAttribute('aria-busy', 'true');
    closeUserMenu(authRoot);
    try {
      await logoutCurrentUser();
    } catch (_) {}
    setDocumentAuthState(null);
    await transitionAuthChrome(authRoot, () => renderGuestAuth(authRoot));
  });
}

function restoreLocalProfileDraft(authRoot, user) {
  try {
    const raw = localStorage.getItem('numberlink.settings.draft');
    if (!raw) return;
    const draft = JSON.parse(raw);
    if (draft?.userId && user?.id && draft.userId !== user.id) return;
    applyLocalProfileToHeader(authRoot, {
      username: draft.username || user.username,
      email: draft.email ?? user.email,
      avatarUrl: user.avatarUrl || draft.avatarUrl || null,
    });
  } catch (_) {}
}

function renderUserAuth(authRoot, user) {
  const name = escapeHtml(user.username || 'Player');
  const emailRaw = user.email ? String(user.email).trim() : '';
  const email = emailRaw ? escapeHtml(emailRaw) : '';
  const initials = escapeHtml(userInitials(user.username));

  authRoot.dataset.authState = 'user';
  authRoot.innerHTML = buildUserMenuHtml(name, email, initials, user.avatarUrl);
  bindUserMenu(authRoot, user);
  restoreLocalProfileDraft(authRoot, user);
}

async function refreshAuthHeader(authRoot) {
  renderAuthSkeleton(authRoot);
  const user = await fetchCurrentUser();
  setDocumentAuthState(user);
  if (user) {
    renderUserAuth(authRoot, user);
  } else {
    renderGuestAuth(authRoot);
  }
}

function clearAuthFormFeedback(overlay) {
  const err = overlay.querySelector('[data-auth-error]');
  const status = overlay.querySelector('[data-auth-status]');
  if (err) err.textContent = '';
  if (status) {
    status.textContent = '';
    status.classList.remove('is-visible');
  }
}

function setAuthSubmitBusy(submitBtn, busy, labels) {
  if (!submitBtn) return;
  submitBtn.disabled = busy;
  submitBtn.classList.toggle('is-busy', busy);
  if (busy) {
    submitBtn.setAttribute('aria-busy', 'true');
    submitBtn.textContent = labels.busy;
  } else {
    submitBtn.removeAttribute('aria-busy');
    submitBtn.textContent = labels.idle;
  }
}

async function onResendVerification(overlay) {
  const btn = overlay.querySelector('[data-auth-resend]');
  const errorEl = overlay.querySelector('[data-auth-error]');
  const status = overlay.querySelector('[data-auth-status]');
  if (!btn || btn.disabled || btn.classList.contains('is-busy') || btn.classList.contains('is-cooling')) {
    return;
  }

  const email = overlay.dataset.verifyEmail || '';
  if (errorEl) errorEl.textContent = '';
  if (status) {
    status.textContent = '';
    status.classList.remove('is-visible');
  }

  setAuthSubmitBusy(btn, true, { idle: 'Resend email', busy: 'Sending…' });

  try {
    await resendVerificationEmail(email);
    btn.textContent = 'Sent';
    btn.disabled = true;
    btn.classList.remove('is-busy');
    await wait(900);
    startVerifyResendCooldown(overlay);
  } catch (err) {
    if (errorEl) errorEl.textContent = err.message || 'Could not resend the email.';
    setAuthSubmitBusy(btn, false, { idle: 'Resend email', busy: 'Sending…' });
  }
}

async function onAuthFormSubmit(auth, overlay, form) {
  const mode = form.getAttribute('data-auth-form');
  if (!validateForm(form, mode)) return;

  const errorEl = form.querySelector('[data-auth-error]');
  const statusEl = overlay.querySelector('[data-auth-status]');
  const submitBtn = form.querySelector('.auth-submit');
  const labelsByMode = {
    login: { idle: 'Log in', busy: 'Signing in…' },
    signup: { idle: 'Create account', busy: 'Creating account…' },
    forgot: { idle: 'Send reset link', busy: 'Sending…' },
    reset: { idle: 'Update password', busy: 'Updating…' },
  };
  const labels = labelsByMode[mode] || { idle: 'Continue', busy: 'Please wait…' };

  clearAuthFormFeedback(overlay);
  setAuthSubmitBusy(submitBtn, true, labels);

  try {
    if (mode === 'forgot') {
      const email = form.querySelector('[name="email"]')?.value.trim() || '';
      await requestPasswordReset(email);
      if (statusEl) {
        statusEl.textContent = 'If an account exists for that email, we sent a reset link. Check your inbox.';
        statusEl.classList.add('is-visible');
      }
      setAuthSubmitBusy(submitBtn, false, labels);
      return;
    }

    if (mode === 'reset') {
      const token = form.querySelector('[data-reset-token]')?.value || '';
      const password = form.querySelector('[name="password"]')?.value || '';
      await submitPasswordReset(token, password);
      await closeAuthAnimated();
      openAuth('login');
      const loginOverlay = document.getElementById('auth-overlay-login');
      const loginStatus = loginOverlay?.querySelector('[data-auth-status]');
      if (loginStatus) {
        loginStatus.textContent = 'Password updated. Log in with your new password.';
        loginStatus.classList.add('is-visible');
      }
      return;
    }

    if (mode === 'signup') {
      const email = form.querySelector('[name="email"]')?.value.trim() || '';
      await registerUser();
      // Until backend stops auto-login on register, clear any session.
      try {
        await logoutCurrentUser();
      } catch (_) {}
      await closeAuthAnimated();
      await refreshAuthHeader(auth);
      openVerifyEmail(email, { sentJustNow: true });
      return;
    }

    const user = await loginUser();
    if (user?.twoFactorRequired) {
      setAuthSubmitBusy(submitBtn, false, labels);
      await closeAuthAnimated();
      openTwoFactorLogin();
      return;
    }
    await closeAuthAnimated();
    if (user) {
      setDocumentAuthState(user);
      await transitionAuthChrome(auth, () => renderUserAuth(auth, user));
    } else {
      await refreshAuthHeader(auth);
    }
  } catch (err) {
    if (mode === 'login' && isUnverifiedEmailError(err)) {
      const loginVal = form.querySelector('[name="login"]')?.value.trim() || '';
      const email = err.email || (loginVal.includes('@') ? loginVal : '');
      setAuthSubmitBusy(submitBtn, false, labels);
      await closeAuthAnimated();
      openVerifyEmail(email || loginVal);
      return;
    }
    if (errorEl) errorEl.textContent = err.message || 'Something went wrong.';
    setAuthSubmitBusy(submitBtn, false, labels);
  }
}

function bindAuthOverlay(auth, overlay) {
  overlay.querySelectorAll('[data-auth-close]').forEach((btn) => {
    btn.addEventListener('click', closeAuth);
  });

  overlay.querySelectorAll('[data-auth-switch]').forEach((btn) => {
    btn.addEventListener('click', () => openAuth(btn.getAttribute('data-auth-switch')));
  });

  overlay.querySelectorAll('[data-auth-forgot]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const loginVal = overlay.querySelector('#auth-login-email')?.value.trim() || '';
      openForgotPassword(loginVal);
    });
  });

  overlay.querySelectorAll('[data-auth-suggest-username]').forEach((btn) => {
    btn.addEventListener('click', () => suggestSignupUsername(overlay));
  });

  overlay.querySelectorAll('[data-oauth]').forEach((btn) => {
    btn.addEventListener('click', () => {
      clearAuthFormFeedback(overlay);
      try {
        loginWithOAuth(btn.getAttribute('data-oauth'));
      } catch (e) {
        setOAuthButtonsBusy(false);
        clearOAuthFlowState();
        const err = overlay.querySelector('[data-auth-error]');
        if (err) err.textContent = e.message || 'OAuth sign-in is unavailable.';
      }
    });
  });

  const form = overlay.querySelector('[data-auth-form]');
  form?.addEventListener('submit', (e) => {
    e.preventDefault();
    onAuthFormSubmit(auth, overlay, form);
  });

  overlay.querySelector('[data-auth-resend]')?.addEventListener('click', () => {
    onResendVerification(overlay);
  });
}

function bindAuthChromeDismiss(auth) {
  document.addEventListener('keydown', (e) => {
    if (e.key !== 'Escape') return;
    if (document.querySelector('[data-auth-overlay="email-password"].is-open, [data-auth-overlay="email-code"].is-open, [data-auth-overlay="email-result"].is-open')) {
      closeEmailChangeOverlays();
    } else if (isSettingsOpen()) closeAccountSettings();
    else if (document.querySelector('.auth-overlay.is-open')) closeAuth();
    else if (auth.querySelector('[data-auth-user-trigger][aria-expanded="true"]')) closeUserMenu(auth);
  });

  document.addEventListener('click', (e) => {
    if (!auth.contains(e.target)) closeUserMenu(auth);
  });
}

function handleOAuthReturn(auth) {
  const linkReturn = consumeOauthLinkReturn();
  if (linkReturn) {
    finishOauthLinkReturn(auth, linkReturn);
    return;
  }

  const oauthError = consumeOAuthReturnParams();
  if (oauthError === 'TWO_FACTOR') {
    openTwoFactorLogin();
    return;
  }
  const oauthPending = takeOAuthPending();

  if (oauthError) {
    openAuth('login');
    const err = document.querySelector('#auth-overlay-login [data-auth-error]');
    if (err) err.textContent = oauthError;
  }

  refreshAuthHeader(auth).then(() => {
    if (!oauthPending || oauthError) return;

    if (!document.body.classList.contains('is-authenticated')) {
      clearOAuthFlowState();
      openAuth('login');
      const err = document.querySelector('#auth-overlay-login [data-auth-error]');
      if (err) {
        err.textContent = 'OAuth sign-in did not complete. Try again or use email and password.';
      }
      return;
    }

    const returnTo = consumeOAuthReturnPath();
    if (returnTo) window.location.assign(returnTo);
  });
}

function initAuthUi() {
  const container = document.querySelector('.header_container');
  if (!container || container.querySelector('.header_auth')) return;

  const auth = document.createElement('div');
  auth.className = 'header_auth';
  container.appendChild(auth);
  renderAuthSkeleton(auth);

  document.body.insertAdjacentHTML(
    'beforeend',
    buildDialog('login')
      + buildDialog('signup')
      + buildForgotPasswordDialog()
      + buildResetPasswordDialog()
      + buildVerifyEmailDialog()
      + buildEmailPasswordDialog()
      + buildEmailCodeDialog()
      + buildEmailResultDialog()
      + buildTwoFactorLoginDialog(),
  );
  document.querySelectorAll('[data-auth-overlay]').forEach((overlay) => bindAuthOverlay(auth, overlay));
  document.querySelector('[data-email-password-form]')?.addEventListener('submit', onEmailPasswordSubmit);
  document.querySelector('[data-email-code-form]')?.addEventListener('submit', onEmailCodeSubmit);
  document.querySelector('[data-totp-form]')?.addEventListener('submit', onTwoFactorLoginSubmit);
  document.querySelector('[data-email-code-resend]')?.addEventListener('click', (e) => {
    const overlay = e.currentTarget.closest('[data-auth-overlay]');
    if (overlay) onEmailCodeResend(overlay);
  });
  document.querySelectorAll('[data-email-change-close]').forEach((btn) => {
    btn.addEventListener('click', () => closeEmailChangeOverlays());
  });
  document.getElementById('auth-overlay-email-code')?.querySelector('[data-settings-email-cancel]')?.addEventListener('click', async () => {
    await onSettingsCancelEmailChange();
    closeEmailChangeOverlays();
  });

  bindAuthChromeDismiss(auth);
  handleOAuthReturn(auth);

  const resetToken = consumeResetTokenFromUrl();
  if (resetToken) {
    openResetPassword(resetToken);
  }
  consumeEmailChangeFromUrl();
}

/** Other pages can open the login dialog (e.g. after 401 on write APIs). */
window.NumberLinkAuth = {
    open: (mode = 'login') => openAuth(mode),
    openVerify: (email, opts) => openVerifyEmail(email, opts),
    openForgot: (email) => openForgotPassword(email),
    openReset: (token) => openResetPassword(token),
    refresh: async () => {
        const el = document.querySelector('.header_auth');
        if (el) await refreshAuthHeader(el);
    },
};

initAuthUi();

/* ——— guest gate ——— */
/**
 * Guest access gate for Leaderboard / Reviews.
 * Skills: baseline-ui (one primary action), frontend-design (invite to act),
 * better-ui (elev + concentric radii), better-typography, better-colors (tokens).
 */

function guestGateId() {
  return 'guest-gate';
}

function lockIcon() {
  return `
<svg viewBox="0 0 24 24" aria-hidden="true" focusable="false">
  <rect x="5" y="11" width="14" height="10" rx="2"/>
  <path d="M8 11V8a4 4 0 0 1 8 0v3"/>
</svg>
`;
}

/**
 * Page-specific copy (UX writing):
 * - Don't assume an account already exists ("Sign in" alone is wrong)
 * - Name both paths: create account OR log in
 * - Lead with the user goal, then the requirement
 */
function guestGateCopy() {
  return {
  leaderboard: {
    eyebrow: 'Save your scores',
    title: 'See rankings with an account',
    text: 'Create an account or log in to open the leaderboard and keep your puzzle results after each win.',
  },
  reviews: {
    eyebrow: 'Player feedback',
    title: 'Join the conversation',
    text: 'Create an account or log in to read reviews, rate NumberLink, and share your own experience.',
  },
  default: {
    eyebrow: 'Account needed',
    title: 'Continue with an account',
    text: 'Create an account or log in to use this part of NumberLink.',
  },
};
}

function resolveGatePage() {
  const path = window.location.pathname.toLowerCase();
  if (path.includes('/reviews')) return 'reviews';
  if (path.includes('/leaderboard')) return 'leaderboard';
  return 'default';
}

function applyGateCopy(root) {
  const copy = guestGateCopy()[resolveGatePage()] || guestGateCopy().default;
  const eyebrow = root.querySelector('.guest-gate__eyebrow');
  const title = root.querySelector('#guest-gate-title');
  const text = root.querySelector('#guest-gate-text');
  if (eyebrow) eyebrow.textContent = copy.eyebrow;
  if (title) title.textContent = copy.title;
  if (text) text.textContent = copy.text;
}

function ensureGate() {
  let root = document.getElementById(guestGateId());
  if (root) return root;

  root = document.createElement('div');
  root.id = guestGateId();
  root.className = 'guest-gate';
  root.hidden = true;
  root.setAttribute('role', 'dialog');
  root.setAttribute('aria-modal', 'true');
  root.setAttribute('aria-labelledby', 'guest-gate-title');
  root.setAttribute('aria-describedby', 'guest-gate-text');
  root.innerHTML = `
    <div class="guest-gate__dialog" tabindex="-1">
      <div class="guest-gate__icon">${lockIcon()}</div>
      <p class="guest-gate__eyebrow"></p>
      <h2 class="guest-gate__title" id="guest-gate-title"></h2>
      <p class="guest-gate__text" id="guest-gate-text"></p>
      <div class="guest-gate__actions">
        <button type="button" class="guest-gate__btn guest-gate__btn--primary" data-gate-auth="login">
          Log in
        </button>
        <button type="button" class="guest-gate__btn guest-gate__btn--ghost" data-gate-auth="signup">
          Sign up
        </button>
      </div>
      <a class="guest-gate__home" href="/">Back to NumberLink</a>
    </div>
  `;

  root.querySelectorAll('[data-gate-auth]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const mode = btn.getAttribute('data-gate-auth') || 'login';
      window.NumberLinkAuth?.open(mode);
    });
  });

  applyGateCopy(root);
  document.body.appendChild(root);
  return root;
}

function setGateVisible(visible) {
  const root = ensureGate();
  root.hidden = !visible;
  document.body.classList.toggle('guest-locked', visible);

  if (visible) {
    requestAnimationFrame(() => {
      root.querySelector('.guest-gate__dialog')?.focus({ preventScroll: true });
    });
  }
}

function revealPage() {
  if (document.body.dataset.pageRevealed === '1') return;
  document.body.dataset.pageRevealed = '1';
  document.body.style.opacity = '1';
}

function applyAuthState(user) {
  setGateVisible(!user);
}

function isAuthenticated() {
  return document.body.classList.contains('is-authenticated');
}

/** Run once after the user is authenticated (skips entirely for guests). */
function whenAuthenticated(callback) {
  let ran = false;
  const run = () => {
    if (ran || !isAuthenticated()) return;
    ran = true;
    callback();
  };

  document.addEventListener('numberlink:auth', (event) => {
    if (event.detail?.user) run();
  });

  if (document.body.dataset.authReady === '1') {
    run();
  }
}

function initGuestGate() {
  ensureGate();

  document.addEventListener('numberlink:auth', (event) => {
    applyAuthState(event.detail?.user ?? null);
    revealPage();
  });

  if (document.body.dataset.authReady === '1') {
    applyAuthState(isAuthenticated() ? { ok: true } : null);
    revealPage();
  }

  window.setTimeout(revealPage, 3000);
}

window.NumberLinkGuest = {
  isAuthenticated,
  whenAuthenticated,
};

initGuestGate();


/* ——— mobile nav ——— */

// Shared mobile hamburger navigation.
// On small screens the nav links collapse behind a burger button;
// desktop layout is untouched. Auth actions stay visible on the right.

function burgerIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" viewBox="0 0 16 16" aria-hidden="true">
  <path fill-rule="evenodd" d="M2.5 12a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5"/>
</svg>`;
}

function mobileCloseIcon() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" viewBox="0 0 16 16" aria-hidden="true">
  <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
</svg>`;
}

function initMobileNav() {
  const container = document.querySelector('.header_container');
  if (!container || container.querySelector('.menu_toggle')) return;

  const btn = document.createElement('button');
  btn.className = 'menu_toggle';
  btn.type = 'button';
  btn.setAttribute('aria-label', 'Open menu');
  btn.setAttribute('aria-expanded', 'false');
  btn.innerHTML = burgerIcon();

  container.prepend(btn);

  btn.addEventListener('click', () => {
    const open = container.classList.toggle('menu-open');
    btn.setAttribute('aria-expanded', String(open));
    btn.setAttribute('aria-label', open ? 'Close menu' : 'Open menu');
    btn.innerHTML = open ? mobileCloseIcon() : burgerIcon();
  });
}

initMobileNav();

/* ——— page ——— */


function main() {
    leaderBoarTimelineButtons();
    sortButtonsConfiguration();

    // No leaderboard API calls for guests — wait until session is confirmed
    window.NumberLinkGuest?.whenAuthenticated(() => {
        generateUserStats();
        pagePagination();
    });
}

main();

function leaderBoarTimelineButtons() {
    const buttons = document.querySelectorAll('.leaderboard_modes div');

    buttons.forEach(button => {
        button.addEventListener('click', () => {

            buttons.forEach(btn => {
                btn.classList.remove('leaderboard_mode');
                btn.classList.add('leaderboard_mode-1');
            });

            button.classList.remove('leaderboard_mode-1');
            button.classList.add('leaderboard_mode');

            pagePagination();
        });
    });
}

function sortButtonsConfiguration() {
    const dropDownElements = document.querySelectorAll('.sort_menu .dropdown-item');
    const mapSortButton = document.getElementById('map-sort-button');

    dropDownElements.forEach(item => {
    item.addEventListener('click', () => {
        if (!mapSortButton) return;
        mapSortButton.textContent = item.textContent;
        mapSortButton.dataset.size = item.dataset.size;

        pagePagination();
    });
});

const sortButtons = document.querySelectorAll('.sort_btn:not(.dropdown-toggle)');

sortButtons.forEach(button => {
    button.addEventListener('click', () => {
        const isActive = button.classList.contains('active');

        sortButtons.forEach(btn => {
            btn.classList.remove('active');
        });

        if (isActive) {
            button.classList.remove('active');
        } else {
            button.classList.add('active');
        }

        pagePagination();
    });
});

}

function resolveLeaderboardFilters() {
  const periodMap = { 'All Time': 'all-time', Monthly: 'month', Weekly: 'week' };
  const sortMap = { Time: 'time', 'Average Time': 'avgElapsedSeconds', 'Average Score': 'avgScore' };

  const periodLabel = document.querySelector('.leaderboard_mode')?.innerHTML;
  const sortLabel = document.querySelector('.active.sort_btn:not(.dropdown-toggle)')?.innerHTML;
  const mapSize = document.getElementById('map-sort-button')?.dataset.size || '';

  return {
    period: periodMap[periodLabel] || '',
    criterion: sortMap[sortLabel] || '',
    mapSize,
  };
}

function showLeaderboardEmpty() {
  const container = document.querySelector('.leaderboard_records');
  container.querySelectorAll('.leaderboard_item:not(.self)').forEach((el) => el.remove());
  container.querySelectorAll('.leaderboard_no_items').forEach((el) => el.remove());

  container.insertAdjacentHTML(
    'beforeend',
    `<div class="leaderboard_no_items row d-flex flex-column justify-content-center align-items-center">
      <div class="lb_empty_state col-11" role="status">
        <div class="lb_empty_icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="M4 6h16"/><path d="M4 12h10"/><path d="M4 18h7"/>
            <circle cx="18" cy="17" r="3"/><path d="M20.2 19.2 22 21"/>
          </svg>
        </div>
        <div class="lb_empty_content">
          <h3 class="lb_empty_title">No scores match</h3>
          <p class="lb_empty_text">Nothing for this period, sort, and map size.</p>
          <button type="button" class="lb_empty_cta" data-empty-cta="reset-filters">Show all-time results</button>
        </div>
      </div>
    </div>`
  );

  document.querySelector('[data-empty-cta="reset-filters"]')?.addEventListener('click', () => {
    const mapBtn = document.getElementById('map-sort-button');
    if (mapBtn) {
      mapBtn.textContent = 'All Sizes';
      delete mapBtn.dataset.size;
    }
    document.querySelectorAll('.sort_btn:not(.dropdown-toggle).active').forEach((btn) => {
      btn.classList.remove('active');
    });
    document.querySelector('.leaderboard_modes .all-time')?.click();
  });
}

function rankMedalClass(rankIndex) {
  if (rankIndex === 0) return 'gold_medal';
  if (rankIndex === 1) return 'silver_medal';
  if (rankIndex === 2) return 'bronze_medal';
  return 'normal_medal';
}

function lbMedalHtml(rank, rankClass) {
  const label = rank == null || rank === '' ? '—' : String(rank);
  return `<span class="lb_medal ${rankClass}">${escapeHtml(label)}</span>`;
}

function fillLbAvatar(el, username, avatarUrl) {
  if (!el) return;
  const src = resolveMediaUrl(avatarUrl);
  el.innerHTML = src
    ? `<img src="${escapeHtml(src)}" alt="">`
    : escapeHtml(userInitials(username));
}

function buildLeaderboardItemHtml(item, realIndex) {
  const rankClass = rankMedalClass(realIndex);
  const mapSize = item.fieldWidth && item.fieldHeight ? `${item.fieldWidth}×${item.fieldHeight}` : '--';
  const player = item.player || '--';
  const src = resolveMediaUrl(item.avatarUrl);
  const avatarInner = src
    ? `<img src="${escapeHtml(src)}" alt="">`
    : escapeHtml(userInitials(player));

  return `
    <div class="leaderboard_item">
      <div class="lb_content">
        <div class="lb_row">
          ${lbMedalHtml(realIndex + 1, rankClass)}
          <div class="lb_avatar">${avatarInner}</div>
          <div class="lb_who">
            <div class="lb_name">${escapeHtml(player)}</div>
            <div class="lb_points">${item.points ?? '--'} points</div>
          </div>
        </div>
        <div class="lb_stats">
          <div class="lb_stat_item"><span class="lb_stat_label">Time:</span><span class="lb_stat_value">${item.elapsedSeconds ? item.elapsedSeconds + 's' : '--'}</span></div>
          <div class="lb_stat_item"><span class="lb_stat_label">Map Size:</span><span class="lb_stat_value">${mapSize}</span></div>
          <div class="lb_stat_item"><span class="lb_stat_label">Avg Time:</span><span class="lb_stat_value">${item.avgElapsedSeconds ? item.avgElapsedSeconds + 's' : '--'}</span></div>
          <div class="lb_stat_item"><span class="lb_stat_label">Avg Score:</span><span class="lb_stat_value">${item.avgScore || '--'}</span></div>
        </div>
      </div>
    </div>
  `;
}

async function pagePagination() {
    if (!window.NumberLinkGuest?.isAuthenticated()) return;

    let allData = [];
    let currentPage = 1;
    const itemsPerPage = 5;

    async function fetchLeaderboard() {
    if (!window.NumberLinkGuest?.isAuthenticated()) return;
    try {
        const { period, criterion, mapSize } = resolveLeaderboardFilters();
        const params = new URLSearchParams();
        if (period) params.set("period", period);
        if (criterion) params.set("criterion", criterion);
        if (mapSize) params.set("mapSize", mapSize);

        const query = `${backendApiUrl()}/score/sort?` + params.toString();

        const response = await fetch(query);
        if (!response.ok) throw new Error('API error');
        
        allData = await response.json();

        if (!allData || allData.length === 0) {
            showLeaderboardEmpty();
            return;
        }

        renderPage(1);

    } catch (error) {
        console.error(error);
    }
}

function renderPage(page) {
    currentPage = page;

    const container = document.querySelector('.leaderboard_records');
    container.querySelectorAll('.leaderboard_item:not(.self)').forEach(el => el.remove());
    container.querySelectorAll('.leaderboard_no_items').forEach(el => el.remove());

    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = allData.slice(startIndex, endIndex);


        pageData.forEach((item, index) => {
        const realIndex = startIndex + index;
        container.insertAdjacentHTML('beforeend', buildLeaderboardItemHtml(item, realIndex));
    });

    updatePaginationUI();
}

function updatePaginationUI() {
    const totalPages = Math.ceil(allData.length / itemsPerPage);

    const startRange = (allData.length === 0) ? 0 : (currentPage - 1) * itemsPerPage + 1;
    const endRange = Math.min(currentPage * itemsPerPage, allData.length);

    document.querySelector('.pg_range').textContent = `${startRange}–${endRange}`;
    document.querySelector('.pg_total').textContent = allData.length;

    const paginationContainer = document.querySelector('.pagination');
    paginationContainer.innerHTML = '';

    const prevBtn = document.createElement('button');
    prevBtn.className = 'page_btn';
    prevBtn.textContent = '‹';
    prevBtn.disabled = (currentPage === 1);
    prevBtn.onclick = () => renderPage(currentPage - 1);
    paginationContainer.appendChild(prevBtn);

    let startPage = 1;
    let endPage = totalPages;

    if (totalPages > 3) {
        if (currentPage <= 2) {
            startPage = 1;
            endPage = 3;
        } else if (currentPage >= totalPages - 1) {
            startPage = totalPages - 2;
            endPage = totalPages;
        } else {
            startPage = currentPage - 1;
            endPage = currentPage + 1;
        }
    }

    for (let i = startPage; i <= endPage; i++) {
        const pageBtn = document.createElement('button');
        pageBtn.className = 'page_btn' + (i === currentPage ? ' active' : '');
        pageBtn.textContent = i;
        pageBtn.onclick = () => renderPage(i);
        paginationContainer.appendChild(pageBtn);
    }

    const nextBtn = document.createElement('button');
    nextBtn.className = 'page_btn';
    nextBtn.textContent = '›';
    nextBtn.disabled = (currentPage === totalPages || totalPages === 0);
    nextBtn.onclick = () => renderPage(currentPage + 1);
    paginationContainer.appendChild(nextBtn);
}


fetchLeaderboard();
}

async function generateUserStats() {
    if (!window.NumberLinkGuest?.isAuthenticated()) return;

    const query = `${backendApiUrl()}/score`;
    try {
            const response = await fetch(query,
            {
                method: "GET",
                credentials: "include"
            });

            if (response.status === 401 || response.status === 403) {
                return;
            }
            if (!response.ok) throw new Error("API error");
            const data = await response.json();

            const nameEl = document.querySelector('.lb_name-1');
            const pointsLine = document.getElementById('self_points_line');
            const medal = document.getElementById('self_rank');
            const player = data.player || 'You';

            nameEl.textContent = player;
            fillLbAvatar(document.getElementById('self_avatar'), player, data.avatarUrl);

            if (data.points != null && data.points !== '' && data.points !== '--') {
                pointsLine.textContent = `${data.points} points`;
            } else {
                pointsLine.textContent = 'No score yet';
            }

            document.querySelector('.lb_stat_value-1.time').innerHTML =
                data.avgElapsedSeconds != null && data.avgElapsedSeconds !== '' ? `${data.avgElapsedSeconds}s` : '—';
            document.querySelector('.lb_stat_value-1.score').innerHTML =
                data.avgScore != null && data.avgScore !== '' ? data.avgScore : '—';

            if (data.rank != null && !Number.isNaN(Number(data.rank))) {
                const rankClass = rankMedalClass(Number(data.rank) - 1);
                if (medal) {
                    medal.textContent = data.rank;
                    medal.classList.remove('self_medal', 'normal_medal', 'bronze_medal', 'silver_medal', 'gold_medal');
                    medal.classList.add(rankClass);
                }
            }

            return;
        } catch (error) {
            console.error("Error:", error);
            return;
        }

}