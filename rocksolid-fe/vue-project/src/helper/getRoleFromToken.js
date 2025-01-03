import VueJwtDecode from 'vue-jwt-decode';

export function getRoleFromToken() {
  const token = localStorage.getItem('token');
  if (!token) return 'none';

  try {
    let decoded = VueJwtDecode.decode(token);
    const role = decoded.role ? decoded.role.trim() : 'none';
    return role || 'none';
  } catch (e) {
    console.error('Invalid token:', e);
    return 'none';
  }
}