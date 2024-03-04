import axios from 'axios';

const API_URL = 'http://localhost:9000';

export default {
    addGamesToWatchedList(userId, gameIds) {
        console.log(`Adding games with IDs: ${gameIds} for user: ${userId} to watched list`);
    
        return axios.post(`${API_URL}/users/${userId}/watched-games/watch`, { gameIds: [gameIds] })
            .then(response => {
                console.log(`Server response: ${JSON.stringify(response.data)}`);
                return response;
            })
            .catch(error => {
                console.error(`Error adding games to watched list: ${error}`);
                throw error;
            });
    },

    removeGamesFromWatchedList(userId, gameIds) {
        console.log(`Removing games with IDs: ${gameIds} for user: ${userId} from watched list`);

        return axios.post(`${API_URL}/users/${userId}/watched-games/unwatch`, { gameIds: [gameIds] })
            .then(response => {
                console.log(`Server response: ${JSON.stringify(response.data)}`);
                return response;
            })
            .catch(error => {
                console.error(`Error removing games from watched list: ${error}`);
                throw error;
            });
    },

    getWatchedGames(userId) {
        return axios.get(`${API_URL}/users/${userId}/watched-games`);
    },

    getUnwatchedGames(userId) {
        return axios.get(`${API_URL}/users/${userId}/watched-games/unwatched`);
    }
}