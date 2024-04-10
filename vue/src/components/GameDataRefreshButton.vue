<template>
    <div v-if="isAdmin" class="refresh-container">
        <button @click="refreshGameData" class="refresh-button">
            <img src="@/assets/refresh.svg" alt="Refresh button" width="20" height="20"
                :class="{ 'spinning': isLoading }">
        </button>
        <transition name="fade">
            <div v-if="status" class="message" aria-live="polite" :data-status="status"
                :class="{ 'is-narrow': windowWidth <= 1024 }">
                <span v-if="status === 'loading'">Refreshing...</span>
                <span v-else-if="status === 'success'">Game data refreshed successfully!</span>
                <span v-else-if="status === 'rate-limited'">Too soon to refresh.</span>
                <span v-else>Failed to refresh game data.</span>
            </div>
        </transition>
    </div>
</template>

<script>
import GameService from '@/services/GameService';
import { mapGetters } from 'vuex';

export default {
    data() {
        return {
            isLoading: false,
            status: '',
            windowWidth: window.innerWidth,
        };
    },
    created() {
        this.handleResize = this.debounce(this.handleResize, 200);
    },
    methods: {
        handleResize() {
            this.windowWidth = window.innerWidth;
        },
        debounce(func, wait) {
            let timeout;
            return function executedFunction(...args) {
                const later = () => {
                    clearTimeout(timeout);
                    func.apply(this, args);
                };
                clearTimeout(timeout);
                timeout = setTimeout(later, wait);
            };
        },
        refreshGameData() {
            console.log("Starting refresh...");
            this.isLoading = true;
            this.status = 'loading';

            const year = new Date().getFullYear();
            const refreshOperation = GameService.refreshGameData(year).then(() => {
                console.log("Refresh operation successful.");
                this.status = 'success';
            }).catch(error => {
                if (error.response && error.response.status === 429) {
                    this.status = 'rate-limited';
                } else {
                    console.error("Error refreshing game data: ", error);
                    this.status = 'failed';
                }
            });

            const minimumDuration = new Promise(resolve => setTimeout(resolve, 1000));

            Promise.all([refreshOperation, minimumDuration]).then(() => {
                this.isLoading = false;
                setTimeout(() => {
                    if (this.status !== 'loading') {
                        this.fadeMessage();
                    }
                }, 5000);
            })
            .finally(() => {
                this.isLoading = false;
            })
        },
        fadeMessage() {
            this.status = '';
        },
    },
    mounted() {
        window.addEventListener('resize', this.handleResize);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.handleResize);
    },
    computed: {
        ...mapGetters(['isAdmin']),
    },
}
</script>

<style scoped>
.refresh-container {
    display: flex;
    align-items: center;
    justify-content: center;
}

.refresh-button {
    flex-shrink: 0;
    margin-left: 0.5rem;
    margin-right: 0;
    padding-right: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background: none;
    border: none;
    cursor: pointer;
}

.refresh-button img {
    width: 20px;
    height: 20px;
    flex-shrink: 0;
}

.refresh-button img.spinning {
    animation: spin 1s linear infinite;
}

.message {
    margin-left: 0.5rem;
    padding-left: 0;
    font-size: 0.875rem;
    color: var(--afl-250);
    font-style: italic;
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.2s;
}

.fade-enter,
.fade-leave-to {
    opacity: 0;
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}

@media (max-width: 1200px) and (min-width: 872px) {
    .refresh-button {
        margin-left: 0.25rem;
    }

    .refresh-button img {
        width: 18px;
        height: 18px;
    }

    .message {
        margin-left: 0.5rem;
    }

    .message span {
        display: none;
    }

    .message[data-status="success"]::before {
        content: "Success";
    }

    .message[data-status="rate-limited"]::before {
        content: "Too soon";
    }

    .message[data-status="failed"]::before {
        content: "Failed";
    }

    .message[data-status="loading"]::before {
        content: "Refreshing...";
    }
} 

@media (max-width: 872px) and (min-width: 768px) {
    .message {
        font-style: normal;
        font-size: 1.25rem;
        margin-left: 0.75rem;
    }

    .message[data-status="success"]::before {
        content: "\2714\FE0E"; /* checkmark */
    }

    .message[data-status="rate-limited"]::before {
        content: "\00231B\FE0E"; /* hourglass */
    }

    .message[data-status="failed"]::before {
       content: "\0026A0\FE0E"; /* warning */
    }

    .message[data-status="loading"]::before {
        content: " ...";
    }

    .message span {
        display: none;
    }
}

@media (max-width: 768px) and (min-width: 520px) {

    .message span {
        display: block;
    }

    .message::before {
        content: none;
    }
}

@media (max-width: 520px) and (min-width: 400px) {
    .message[data-status="success"]::before {
        content: "Success";
    }

    .message[data-status="rate-limited"]::before {
        content: "Too soon";
    }

    .message[data-status="failed"]::before {
        content: "Failed";
    }

    .message[data-status="loading"]::before {
        content: "Refreshing...";
    }

    .message span {
        display: none;
    }
}

@media (max-width: 400px) {
    .message {
        font-style: normal;
        font-size: 1.25rem;
        margin-left: 0.5rem;
    }

    .message[data-status="success"]::before {
        content: "\2714\FE0E"; /* checkmark */
    }

    .message[data-status="rate-limited"]::before {
        content: "\00231B\FE0E"; /* hourglass */
    }

    .message[data-status="failed"]::before {
       content: "\0026A0\FE0E"; /* warning */
    }

    .message[data-status="loading"]::before {
        content: " ...";
    }

    .message span {
        display: none;
    }
}

</style>