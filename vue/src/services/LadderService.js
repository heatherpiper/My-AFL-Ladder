import axios from 'axios';
import store from '../store';

const API_URL = 'https://later-ladder-backend.onrender.com';

export default {
    getLadder() {
        const userId = store.state.user.id;
        return axios.get(`${API_URL}/ladder/${userId}`);
    },
    resetLadder() {
        const userId = store.state.user.id;
        return axios.post(`${API_URL}/ladder/${userId}/reset`);
    }
};