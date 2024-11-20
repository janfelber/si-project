import axios from 'axios';
import { RegisterRequest } from "@/models/register-request";
import { AuthenticationReponse } from "@/models/authentication-reponse";

const baseUrl = 'http://localhost:8080/api/v1/auth';

class AuthenticationService {
  // Register a user
  register(registerRequest: RegisterRequest) {
    return axios.post<AuthenticationReponse>(`${baseUrl}/register`, registerRequest);
  }
}

export default new AuthenticationService();