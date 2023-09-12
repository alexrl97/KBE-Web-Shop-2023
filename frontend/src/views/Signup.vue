<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center pt-3">
          <!-- display logo -->
        </div>
      </div>
  
      <!-- header -->
  
      <div class="row">
        <div class="col-12 justify-content-center d-flex pt-3">
          <div id="signup" class="flex-item border">
            <h2 v-if="role === 'storehouse'" class="pt-4 pl-4">Mitarbeiterregistrierung</h2>
            <h2 v-else class="pt-4 pl-4">Kundenregistrierung</h2>
            <form @submit="signup" class="pt-4 pl-4 pr-4">
              <div class="form-group">
                <label for="Email">E-Mail</label>
                <input
                  type="email"
                  v-model="email"
                  class="form-control"
                  required
                />
              </div>
              <div class="form-row">
                <div class="col">
                  <div class="form-group">
                    <label> Vorname</label>
                    <input
                      type="text"
                      v-model="firstName"
                      class="form-control"
                      required
                    />
                  </div>
                </div>
                <div class="col">
                  <div class="form-group">
                    <label> Nachname</label>
                    <input
                      type="text"
                      v-model="lastName"
                      class="form-control"
                      required
                    />
                  </div>
                </div>
              </div>

              <div v-if="role !== 'storehouse'">
              <div class="form-group">
                <label for="Company">Firma</label>
                <input
                    type="text"
                    v-model="company"
                    class="form-control"
                />
              </div>

              <div class="form-row">
                <div class="col">
                  <div class="form-group">
                    <label> Straße</label>
                    <input
                        type="text"
                        v-model="street"
                        class="form-control"
                        required
                    />
                  </div>
                </div>
                <div class="col">
                  <div class="form-group">
                    <label> Hausnummer</label>
                    <input
                        type="text"
                        v-model="houseNumber"
                        class="form-control"
                        required
                    />
                  </div>
                </div>
              </div>
              <div class="form-row">
                <div class="col">
                  <div class="form-group">
                    <label> PLZ</label>
                    <input
                        type="text"
                        v-model="zip"
                        class="form-control"
                        required
                    />
                  </div>
                </div>
                <div class="col">
                  <div class="form-group">
                    <label> Stadt</label>
                    <input
                        type="text"
                        v-model="city"
                        class="form-control"
                        required
                    />
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label> Land</label>
                <input
                    type="text"
                    v-model="country"
                    class="form-control"
                    required
                />
              </div>

          </div>
  
              <!-- password -->
              <div class="form-group">
                <label for="Password"> Passwort</label>
                <input
                  type="password"
                  v-model="password"
                  class="form-control"
                  required
                />
              </div>
  
              <!-- confirm password -->
              <div class="form-group">
                <label for="Password"> Passwort bestätigen</label>
                <input
                  type="password"
                  v-model="confirmPassword"
                  class="form-control"
                  required
                />
              </div>
  
              <button class="btn btn-primary mt-2">Erstellen</button>
            </form>
          </div>
        </div>
      </div>
  
      <!-- form -->
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
        firstName: null,
        lastName: null,
        company:null,
        role:null,
        street: null,
        houseNumber: null,
        city: null,
        zip: null,
        country: null,
        password: null,
        confirmPassword: null,
      };
    },
    methods: {
      async signup(e) {
        e.preventDefault();
        if (this.password === this.confirmPassword) {
          if (this.role === '' || this.role === null) this.role = 'customer';

          const hashedPassword = CryptoJS.SHA256(this.password).toString();

          const user = {
            email: this.email,
            firstName: this.firstName,
            lastName: this.lastName,
            company: this.company,
            role: this.role,
            password: hashedPassword,
            street: this.street,
            houseNumber: this.houseNumber,
            city: this.city,
            zip: this.zip,
            country: this.country,
          };

          console.log("user", user);
          await axios
              .post(`${this.baseURL}user/signup`, user)
              .then(() => {
                this.$router.replace("/");
                swal({
                  text: "Registrierung erfolgreich, bitte einloggen",
                  icon: "success",
                });
              })
              .catch((err) => console.log("err", err));
        } else {
          swal({
            text: "Passwörter stimmen nicht überein",
            icon: "error",
          });
        }
      },
    },
    mounted() {
      this.role = localStorage.getItem("role");
    },
  };
  </script>
  <style scoped>
  .btn-primary {
    background-color: #f0c14b;
    color: #000;
  }
  @media screen and (min-width: 992 px) {
    #signup {
      width: 40%;
    }
  }
  </style>