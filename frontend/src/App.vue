<template>
  <div>
    <Navbar
        :key="navBarReloadKey"
        :cartCount="cartCount"
        :categories="categories"
    @resetCartCount="resetCartCount"
    />
    <router-view
        v-if="categories && products"
        style="min-height: 60vh"
        :baseURL="baseURL"
        :categories="categories"
        :products="products"
        :key="$route.fullPath"
        @fetchData="fetchData"
    ></router-view>
    <Footer />
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer';
import axios from 'axios';
export default {
  components: { Navbar, Footer },
  data() {
    return {
      baseURL: "http://localhost:8080/",
      products: null,
      categories: null,
      cartCount: 0,
      navBarReloadKey: 0,
    };
  },
  methods: {
    async fetchData() {
      await axios
        .get(this.baseURL + 'category/list/')
        .then((res) => {
          this.categories = res.data;
        })
        .catch((err) => console.log('err"´', err));
      await axios
        .get(this.baseURL + 'product/')
        .then((res) => {
          this.products = res.data;
        })
        .catch((err) => console.log('err', err));
      this.token = localStorage.getItem('token');
      if (this.token) {
        axios
          .get(`${this.baseURL}cart/?token=${this.token}`)
          .then((res) => {
            const result = res.data;
            this.cartCount = result.cartItems.length;
          })
          .catch((err) => console.log('err', err));
      }
      this.navBarReloadKey++;
    },
    resetCartCount() {
      this.cartCount = 0;
    },
  },
  mounted() {
    this.token = localStorage.getItem('token');
    this.fetchData();
  },
};
</script>

<style>
html {
  overflow-y: scroll;
}
</style>

Navbar