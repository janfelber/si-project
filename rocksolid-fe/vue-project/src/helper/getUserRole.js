import axios from 'axios';

export async function getUserRole() {
  const token = localStorage.getItem('token');

  try {
    const response = await axios.get("http://localhost:8080/api/v1/user/me/role", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Failed to verify user role:", error);
    return null;
  }
}