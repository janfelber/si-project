import axios from 'axios';

const baseUrl = 'http://localhost:8080/api/v1/user';

class UserService {
    // Delete a user by ID
    deleteUser(id: number) {
        return axios.delete(`${baseUrl}/${id}`);
    }


}

export default new UserService();