const CONFIRMED_FLAG = 'nl_email_just_confirmed';
const CONFIRM_ERROR_FLAG = 'nl_email_confirm_error';

function backendApiUrl() {
  return '/api';
}

async function verifyEmail(token) {
  const response = await fetch(`${backendApiUrl()}/verify-email`, {
    method: 'POST',
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    body: JSON.stringify({ token }),
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'This confirmation link is invalid or has expired.');
  }
  return data;
}

function goHome() {
  window.location.replace('/');
}

async function main() {
  const params = new URLSearchParams(window.location.search);
  const token = (params.get('token') || '').trim();

  try {
    sessionStorage.removeItem(CONFIRM_ERROR_FLAG);

    if (!token) {
      sessionStorage.setItem(
        CONFIRM_ERROR_FLAG,
        'Open the confirmation link from your email to finish setting up your account.',
      );
      goHome();
      return;
    }

    const user = await verifyEmail(token);
    sessionStorage.setItem(CONFIRMED_FLAG, user?.username || '1');
    try {
      localStorage.setItem('nl_email_verified_ping', String(Date.now()));
    } catch (_) {}
    goHome();
  } catch (err) {
    sessionStorage.setItem(
      CONFIRM_ERROR_FLAG,
      err?.message || 'This confirmation link is invalid or has expired.',
    );
    goHome();
  }
}

main();
