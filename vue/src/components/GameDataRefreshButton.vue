<template>
    <div v-if="isAdmin" class="refresh-container">
        <button @click="refreshGameData" class="refresh-button">
            <img src="@/assets/refresh.svg" alt="Refresh button" width="24" height="24"
                :class="{ 'spinning': isLoading }">
        </button>
        <div v-if="message" class="message">
            {{ message }}
        </div>
    </div>
</template>

<script>
import GameService from '@/services/GameService';
import { mapGetters } from 'vuex';

export default {
    data() {
        return {
            message: '',
            isLoading: false,
        };
    },
    computed: {
        ...mapGetters(['isAdmin']),
    },
    methods: {
        refreshGameData() {
            this.isLoading = true;
            this.message = "Refreshing game data...";

            const year = new Date().getFullYear();
            const refreshOperation = GameService.refreshGameData(year).then(() => {
                return "Game data refreshed successfully!";
            }).catch(error => {
                console.error("Error refreshing game data:", error);
                return 'Failed to refresh game data.';
            });

            const minimumDuration = new Promise(resolve => setTimeout(resolve, 1000));

            Promise.all([refreshOperation, minimumDuration])
                .then(([resultMessage]) => {
                    this.message = resultMessage;
                    setTimeout(() => this.fadeMessage(), 5000);
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },
        fadeMessage() {
            const messageElement = this.$el.querySelector('.message');
            if (messageElement) {
                messageElement.classList.add('fade-out');
                setTimeout(() => {
                    this.message = '';
                    if (this.$el.querySelector('.message')) {
                        messageElement.classList.remove('fade-out');
                    }
                }, 1000);
            }
        },
    }
}
</script>

<style scoped>
.refresh-container {
    display: flex;
    align-items: center;
}

.refresh-button {
    background: none;
    border: none;
    cursor: pointer;
    margin-left: 0.5rem;
}

.refresh-button img.spinning {
    animation: spin 1s linear infinite;
}

.message {
    color: var(--afl-250);
    font-size: 0.875rem;
    margin-left: 0.5rem;
    font-style: italic;
}

.fade-out {
    animation: fadeOutAnimation 1s forwards;
}

@keyframes fadeOutAnimation {
    from {
        opacity: 1;
    }

    to {
        opacity: 0;
    }
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}
</style>