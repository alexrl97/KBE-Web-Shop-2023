<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h3 class="pt-3">
            Warenkorb
          </h3>
        </div>
      </div>
  
      <!-- loop over the cart items and display -->
  
      <div
      v-for="cartItem in cartItems"
      :key="cartItem.id"
      class="row mt-2 pt-3 justify-content-around"
    >
        <div class="col-2"></div>
        <div class="col-md-3 embed-responsive embed-responsive-16by9">
          <img
            :src="cartItem.product.imageURL"
            alt=""
            class="w-100 card-image-top embed-responsive-item"
            style="object-fit: cover"
          />
        </div>
  
        <!-- display product name, quantity -->
        <div class="col-md-5 px-3">
          <div class="card-block px-3">
            <h6 class="card-title">
                <router-link
              :to="{ name: 'ShowDetails', params: { id: cartItem.product.id } }"
            >
              {{ cartItem.product.name }}
            </router-link>
            </h6>
  
            <p class="mb-0 font-weight-bold" id="item-price">
              {{ cartItem.product.price }}€ pro Karte
            </p>
            <p class="mb-0" style="float:left">
            Anzahl:{{ cartItem.quantity }}
          </p>
          </div>
          <p class="mb-0" style="float:right">
            Preis:
            <span class="font-weight-bold">
             {{ cartItem.product.price * cartItem.quantity }}€
            </span>
          </p>
          <br />
        <a href="#" class="text-right"  @click="deleteItem(cartItem.id) "
          >&emsp;Aus dem Warenkorb entfernen
        </a>
        </div>
        <div class="col-2"></div>
        <div class="col-12"><hr /></div>
      </div>

      <div style="text-align: center">
        <br>Versandaddresse:
        <br>
        <p>
          <br>{{this.firstName}} {{this.lastName}}
          <br v-if="this.company !== ''" >{{this.company}}
          <br>{{this.street}} {{this.houseNumber}}
          <br>{{this.zip}} {{this.city}}
          <br>{{this.country}}
        </p>

        <router-link  class="btn btn-success mt-2" type="button" :to="{name : 'EditAddress'}">Aktualisieren</router-link>
      </div>

      <!-- display the price -->
      <div class="total-cost pt-2 text-right">
        <h5>Gesamtpreis: {{ totalCost.toFixed(2) }}€</h5>
        <button type="button" class="btn btn-primary confirm" @click="checkout">Bezahlen</button>
      </div>
    </div>
  </template>
  <script>
  import axios from 'axios';
  export default {
    data() {
      return {
        cartItems: [],
        token: null,
        totalCost: 0,
        firstName: null,
        lastName: null,
        company: null,
        street: null,
        houseNumber: null,
        city: null,
        zip: null,
        country: null,
      };
    },
    props: ['baseURL'],
    methods: {
      // fetch All items in cart
      listCartItems() {
        axios
          .get(`${this.baseURL}cart/?token=${this.token}`)
          .then((res) => {
            const result = res.data;
            this.cartItems = result.cartItems;
            this.totalCost = result.totalCost;
          })
          .catch((err) => console.log('err', err));
      },

      getLatestAddress(){
        axios
            .get(`${this.baseURL}address/latest?token=${this.token}`)
            .then((res) => {
              const result = res.data;
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

      deleteItem(itemId) {
      axios
        .delete(`${this.baseURL}cart/delete/${itemId}/?token=${this.token}`)
        .then((res) => {
          if (res.status == 200) {
            this.$router.go(0);
          }
        })
        .catch((err) => console.log('err', err));
        },

      checkout() {
      this.$router.push({ name: 'Checkout' });
      },

    },
    mounted() {
      this.token = localStorage.getItem('token');
      this.listCartItems();
      this.getLatestAddress();
    },
  };
  </script>
  <style scoped>
  h4,
  h5 {
    color: #484848;
    font-size: 700;
  }
  </style>