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
          <h2 style="text-align: center" class="pt-4 pl-4">Addressbearbeitung</h2>
          <form @submit="signup" class="pt-4 pl-4 pr-4">
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

            <button class="btn btn-success mt-2" type="button" @click="updateAddress">Aktualisieren</button>
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
//import swal from "sweetalert";

export default {
  props: ["baseURL"],
  data() {
    return {
      token: null,
      address:null,
      firstName: null,
      lastName: null,
      company:null,
      role:null,
      street: null,
      houseNumber: null,
      city: null,
      zip: null,
      country: null,
    };
  },
  methods: {
    getLatestAddress(){
      axios
          .get(`${this.baseURL}address/latest?token=${this.token}`)
          .then((res) => {
            const result = res.data;
            this.address = result;
            this.firstName = result.firstName;
            this.lastName = result.lastName;
            this.company = result.company;
            this.street = result.street;
            this.houseNumber = result.houseNumber;
            this.city = result.city;
            this.zip = result.zip;
            this.country = result.country;
          })
          .catch((err) => console.log('err', err));
    },

    async updateAddress(){
      this.address.firstName = this.firstName
      this.address.lastName = this.lastName
      this.address.company = this.company
      this.address.street = this.street
      this.address.houseNumber = this.houseNumber
      this.address.city = this.city
      this.address.zip = this.zip
      this.address.country = this.country

      await axios.post(`${this.baseURL}address/update?token=${this.token}`,this.address)
          .then(() => {
            swal({
              text: "Addresse aktualisiert",
              icon: "success"
            })
          }).catch(err => console.log('err', err));
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.role = localStorage.getItem("role");
    this.getLatestAddress();
  },
};
</script>

<style scoped>

</style>