<template>
  <button class="google-signin-button" @click="signInWithGoogle">
    <img :src="googleLogo" alt="Google logo" />Sign in with Google
  </button>
</template>
  
<script>
import googleLogo from '@/assets/google-logo.svg';
export default {
    name: 'GoogleSignInButton',
    data() {
        return {
            googleLogo,
        };
    },
    methods: {
        async signInWithGoogle() {
            try {
                const googleUser = await this.$gAuth.signIn();
                const idToken = googleUser.getAuthResponse().id_token;
                this.$store.dispatch('loginWithGoogle', { idToken });
            } catch (error) {
                console.error('Error during Google Sign-In:', error);
            }
        },
    },
};
</script>

<style scoped>

.google-signin-button {
    width: 100%;
    height: 40px;
    padding: 10px;
    border: 1px solid var(--afl-900);
    border-radius: 8px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 12px auto 24px;
    font-size: 16px;
    text-align: center;
    box-sizing: border-box;
    background-color: var(--afl-200);
    color: var(--afl-900);
    transition: box-shadow 0.1s ease, transform 0.1s ease;
}

.google-signin-button:hover {
    box-shadow: 0 0 2px rgba(0, 0, 0, 0.1), 0 2px 2px rgba(0, 0, 0, 0.1);
    transform: scale(1.01);
}

.google-signin-button img {
    height: 24px;
    margin-right: 10px;
}

</style>