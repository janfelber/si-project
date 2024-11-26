import axios from 'axios';

const baseUrl = 'http://localhost:8080/api/v1/auth';

class AuthenticationService {
  // Register a user
  register(registerRequest) {
    return axios.post(`${baseUrl}/register`, registerRequest)
  }

  //Login user
  login(loginRequest) {
    return axios.post(`${baseUrl}/login`, loginRequest);
  }
}

export default new AuthenticationService();