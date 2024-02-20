import axios from 'axios';
import store from '../store';

export default {
    async getLadder() {
        const userId = store.state.user.id;
        try {
            const response = await axios.get(`http://localhost:8080/ladder/${userId}`);
            return response.data;
        } catch (error) {
            console.error('Error: ', error);
            throw error;
        }
    },
};