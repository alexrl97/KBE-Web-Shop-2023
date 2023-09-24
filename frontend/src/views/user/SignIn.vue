<template>
  <div class="container">
    <div class="row">
      <div class="col-12 justify-content-center d-flex flex-row pt-5">
        <div id="signin" class="flex-item border-0">
          <h2 class="pt-4" style="margin-left: 5%">Einloggen</h2>
          <form @submit="signin" class="form-group pt-4 pl-4 pr-4">
            <div class="form-group">
              <label>E-Mail</label>
              <input v-model="email" type="email" class="form-control" />
            </div>
            <div class="form-group">
              <label>Passwort</label>
              <input v-model="password" type="password" class="form-control" />
            </div>
            <button class="btn btn-primary mt-2 py-0">Weiter</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
  <script>
  import axios from "axios";
  import swal from "sweetalert";
  import CryptoJS from 'crypto-js';
  export default {
    props: ["baseURL"],
    data() {
      return {
        email: null,
        password: null,
      };
    },
    methods: {
      async signin(e) {
        e.preventDefault();

        const hashedPassword = CryptoJS.SHA256(this.password).toString();

        const body = {
          email: this.email,
          password: hashedPassword,
        };

        try {
          const res = await axios.post(`${this.baseURL}user/signin`, body);

          localStorage.setItem("token", res.data.token);
          localStorage.setItem("role", res.data.role);

          swal({
            text: "Anmelden erfolgreich",
            icon: "success",
          }).then(() => {
            this.$router.replace("/");
            this.$router.push({ name: "Home" });
            this.$emit("fetchData");
          });
        } catch (err) {
          swal({
            text: "Anmelden fehlgeschlagen. Bitte überprüfen Sie Ihre Anmeldeinformationen.",
            icon: "error",
          });
          console.error("Fehler beim Anmelden:", err);
        }
      },
    },
  };
  </script>
  <style scoped>
  .btn-primary {
    background-color: #f0c14b;
    color: black;
  }
  @media screen and (min-width: 992px) {
    #signin {
      width: 40%;
    }
  }
  </style>