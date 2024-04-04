<template>
  <div id="register" class="register">
    <form @submit.prevent="register">
      <div class="logo-container">
        <img src="@/assets/LaterLadder-Lettermark.svg" alt="Later Ladder lettermark">
      </div>
      <h1>Sign up</h1>
      <div role="alert" v-if="registrationErrors">
        {{ registrationErrorMsg }}
      </div>
      <div class="form-input-group">
        <input type="text" id="username" placeholder="Username" v-model="user.username" autocomplete="username" required />
      </div>
      <div class="form-input-group">
        <input type="password" placeholder="Password" id="password"  v-model="user.password" @input="clearErrors" autocomplete="new-password" required />
      </div>
      <div class="form-input-group">
        <input type="password" id="confirmPassword" placeholder="Confirm password" v-model="user.confirmPassword" @input="clearErrors" autocomplete="new-password" required />
      </div>
      <button type="submit">Register</button>
      <GoogleSignInButton />
      <p>Already have an account? <router-link :to="{ name: 'login' }">Log in.</router-link></p>
    </form>
  </div>
</template>


<script>
import GoogleSignInButton from '../components/GoogleSignInButton.vue';
import authService from '../services/AuthService';

export default {
  name: 'register',
  components: {
    GoogleSignInButton,
  },
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      this.clearErrors();

      if (this.user.password !== this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
        return;
      }

      if (!this.validatePassword()) {
        return;
      }

      authService
        .register(this.user)
        .then((response) => {
          if (response.status == 201) {
            this.$router.push({
              path: '/login',
              query: { registration: 'success' },
            });
          }
        })
        .catch((error) => {
          this.registrationErrors = true;
          if (error.response && error.response.status === 400) {
            this.registrationErrorMsg = error.response.data || 'Username is already taken.';
          } else {
            this.registrationErrorMsg = 'An error occurred during registration.';
          }
        });
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
    validatePassword() {
      this.clearErrors();
      const errors = [];

      if (!this.user.password) {
        errors.push('Password is required.');
      } else {
          if (this.user.password.length < 8) {
            errors.push('Password must be at least 8 characters long.');
          }
          if (this.user.password.length > 30) {
            errors.push('Password must be at most 30 characters long.');
          }
          if (!/[A-Z]/.test(this.user.password)) {
            errors.push('Password must contain at least one uppercase letter.');
          }
          if (!/[a-z]/.test(this.user.password)) {
            errors.push('Password must contain at least one lowercase letter.');
          }
          if (!/[0-9]/.test(this.user.password)) {
            errors.push('Password must contain at least one digit.');
          }
          if (!/[!@#$%^&*.]/.test(this.user.password)) {
            errors.push('Password must contain at least one special character (!@#$%^&*.)');
          }
      }
      if (errors.length > 0) {
        this.registrationErrors = true;
        this.registrationErrorMsg = errors.join(' ');
        return false;
      }
      return true;
    },
  },
};
</script>

<style scoped>

.register {
  max-width: 400px;
  margin: 2em auto; 
  padding: 1.8em;
  background-color: var(--afl-200);
  border: 1px solid var(--afl-800);
  border-radius: 12px; 
  box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 1.5em;
}

.logo-container img {
  max-width: 80px;
}

.register h1 {
  color: var(--afl-600);
  text-align: center;
}

.form-input-group {
  margin-bottom: 12px;
  display: flex;
  justify-content: center;
}

input[type="text"],
input[type="password"] {
  width: calc(100% - 20px);
  height: 40px;
  padding: 0 10px;
  background-color: #e1e6e9;
  border: none;
  border-radius: 8px; 
  color: var(--afl-900); 
  display: block;
  margin: auto;
}

button[type="submit"] {
  width: 100%;
  height: 40px;
  padding: 10px;
  background-color: var(--afl-500); 
  border: none; 
  border-radius: 8px; 
  color: var(--afl-100); 
  cursor: pointer;
  display: block;
  margin: 12px auto;
  font-size: 16px;
  transition: background-color 0.1s ease-in-out, box-shadow 0.1s ease, transform 0.1s ease;
}

button[type="submit"]:hover {
  background-color: var(--afl-450);
  box-shadow: 0 0 2px rgba(0, 0, 0, 0.1), 0 2px 2px rgba(0, 0, 0, 0.1);
  transform: scale(1.01);
}

div[role="alert"] {
  color: #ff6b6b;
  margin-bottom: 16px;
  text-align: center;
}

p {
  text-align: center;
  color: var(--afl-800);
}

p a{
  color: var(--afl-500);
}

p a:visited {
  color: var(--afl-500);
}

</style>
