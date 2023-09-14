<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <!-- Navbar content -->
      <!--    Logo-->
      <router-link class="navbar-brand" :to="{ name: 'Home' }">
        <img id="logo" src="../assets/logo.png" />
      </router-link>
      <!--    Burger Button-->
      <button
        class="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <!--
        <form class="form-inline ml-auto mr-auto">
          <div class="input-group">
            <input size="80" type="text" class="form-control" placeholder="Suche Karten" aria-label="Username" aria-describedby="basic-addon1">
            <div class="input-group-prepend">
            <span class="input-group-text" id="search-button-navbar">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
                <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>
              </svg>
            </span>
            </div>
          </div>
        </form>
        -->

        <!-- dropdown for browse -->
        <ul class="navbar-nav ml-auto">

          <!--      Admin drop down-->
          <li class="nav-item dropdown">
            <a class="nav-link text-light dropdown-toggle" href="#" id="navbarDropdownAdmin" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Karten
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdownAdmin">
              <router-link class="dropdown-item" :to="{name : 'AdminCategory'}">Kartentypen</router-link>
              <router-link class="dropdown-item" :to="{name : 'AdminProduct'}">Karten</router-link>
            </div>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link text-light dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Konto
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <router-link class="dropdown-item" v-if="role === 'customer'"  :to="{name : 'WishList'}" >Wunschliste</router-link>
              <router-link class="dropdown-item" v-if="token" :to="{name : 'OrderHistory'}" >Bestellungen</router-link>
              <router-link class="dropdown-item" v-if="!token" :to="{name: 'SignIn'}">Einloggen</router-link>
              <router-link class="dropdown-item" v-if="!token" :to="{name: 'SignUp'}">Registrieren</router-link>
              <router-link class="dropdown-item" v-if="role === 'storehouse'" :to="{name: 'SignUp'}">Mitarbeiter</router-link>
              <a class="dropdown-item" v-if="token" href="#" @click="signout">Abmelden</a>
            </div>
          </li>
          <div v-if="role !== 'storehouse'">
            <li class="nav-item">
              <div id="cart" style="position: relative;  margin-left: 35px;"> <!-- Hier habe ich margin-left geändert -->
                <span id="nav-cart-count">{{ cartCount }}</span>
                <router-link class="text-light" :to="{ name: 'Cart' }">
                  <i class="fa fa-shopping-cart" style="font-size: 36px;"></i>
                </router-link>
              </div>
            </li>
          </div>
        <div v-else style="margin-right: 75px"></div>
      </ul>
      </div>
    </nav>
  </template>
  <script>
  import swal from "sweetalert";
  export default {
    name: "Navbar",
    props: ["cartCount"],
    data() {
    return {
      token: null,
      role:null
    };
  },
    methods: {
      signout() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        this.token = null;
        this.role = null;
        swal({
        text: "Ausloggen erfolgreich",
        icon: "success",
          }).then(() => {
          this.$emit("resetCartCount");
          this.$router.push({ name: "SignIn" });
        }).catch((err) => console.log("err", err));
      },
    },
    mounted() {
      this.token = localStorage.getItem("token");
      this.role = localStorage.getItem("role");
    },
    };
  </script>
  <style scoped>
  #logo {
    width: 150px;
    margin-left: 20px;
    margin-right: 20px;
  }
  .nav-link {
  color: rgba(255, 255, 255);
  }
  #search-button-navbar {
  background-color: #febd69;
  border-color: #febd69;
  border-top-right-radius: 2px;
  border-bottom-right-radius: 2px;
}
#nav-cart-count {
  background-color: red;
  color: white;
  border-radius: 50%;
  height: 15px;
  width: 15px;
  font-size: 15px;
  align-items: center;
  display: flex;
  justify-content: center;
  position: absolute;
  margin-left: 10px;
}
  </style>