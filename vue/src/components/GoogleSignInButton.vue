<template>
  <button @click="signInWithGoogle">Sign in with Google</button>
</template>
  
<script>
export default {
    name: 'GoogleSignInButton',
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