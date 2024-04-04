import axios from 'axios';

export default {
    loginWithGoogle(idToken) {
        return axios.post('/auth/google', { idToken });
    },

    exchangeAuthorizationCode(authorizationCode) {
        return axios.post('/auth/google/exchange', { code: authorizationCode });
    }
};