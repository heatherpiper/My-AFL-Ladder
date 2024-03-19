<template>
  <div id="login" class="login">
    <form @submit.prevent="login">
      <h1 >Please sign in</h1>
      <div role="alert" v-if="invalidCredentials">
        Invalid username or password
      </div>
      <div role="notice" v-if="this.$route.query.registration">
        Thank you for registering. Please sign in.
      </div>
      <div class="form-input-group">
        <input type="text" id="username" placeholder="Username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <input type="password" id="password" placeholder="Password" v-model="user.password" required />
      </div>
      <button type="submit">Sign in</button>
      <p>Need an account? <router-link :to="{ name: 'register' }">Sign up.</router-link></p>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "login",
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch(error => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>

.login {
    max-width: 400px;
    margin: 2em auto; 
    padding: 1.8em;
    background-color: var(--afl-200);
    border: 1px solid var(--afl-800);
    border-radius: 12px; 
    box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);
}

.login h1 {
  color: var(--afl-600);
  text-align: center;
}

.form-input-group {
  margin-bottom: 10px;
  display: flex;
  justify-content: center;
}

input[type="text"],
input[type="password"] {
  width: calc(100% - 20px); 
  padding: 10px;
  background-color: #e1e6e9;
  border: none;
  border-radius: 8px; 
  color: var(--afl-900); 
  display: block;
  margin: auto;
}

button[type="submit"] {
  width: 100%; 
  padding: 10px;
  background-color: var(--afl-500); 
  border: none; 
  border-radius: 8px; 
  color: var(--afl-100); 
  cursor: pointer;
  display: block;
  margin: 20px auto;
  font-size: 16px;
}

button[type="submit"]:hover {
  background-color: var(--afl-450);
}

div[role="alert"] {
  color: #ff6b6b;
  margin-bottom: 16px;
  text-align: center;
}

div[role="notice"] {
  color: var(--afl-600);
  margin-bottom: 16px;
  text-align: center;
}

p {
  text-align: center;
  color: var(--afl-800);
}

p router-link {
  color: var(--afl-600);
}

*:visited {
  color: var(--afl-600);
}

</style>