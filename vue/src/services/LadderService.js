import axios from 'axios';
import store from '../store';

export default {
    getLadder() {
        const userId = store.state.user.id;
        return axios.get(`http://localhost:9000/ladder/${userId}`);
    }
};