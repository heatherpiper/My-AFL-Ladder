<template>
    <div>
        <p v-if="!error">Processing Google Sign-In...</p>
        <p v-else>{{ error }}</p>
    </div>
</template>

<script>
export default {
    name: 'GoogleOAuthCallback',
    mounted() {
        const authorizationCode = this.$route.query.code;

        // Send the authorization code to the back end for token exchange
        this.$store.dispatch('exchangeAuthorizationCode', authorizationCode)
            .then(() => {

                // Redirect to the main page after successful token exchange
                this.$router.push('/');
            })
            .catch(error => {
                console.error('Error during token exchange:', error);
                this.error = 'An error occurred during Google Sign-In. Please try again.';
            });
    },
};
</script>