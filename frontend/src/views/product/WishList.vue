<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center" style="margin-top: 10px">
          <h4 class="pt-3">Wunschliste</h4>
        </div>
      </div>
  
      <div class="row">
        <div
          v-for="product of products"
          :key="product.id"
          class="col-lg-2 col-md-3 col-sm-4 col-6 pt-3 justify-content-around d-flex">
          <ProductBox :product="product"> </ProductBox>
          <button @click="deleteFromWishlist(id)"  class="btn btn-danger" style="position: absolute; bottom: 0; left: 0; margin-left: 10px;transform: scale(0.6);">Löschen</button>
        </div>
      </div>
    </div>
  </template>
  <script>
  import axios from "axios";
  import ProductBox from "../../components/ProductBox.vue";
  import swal from "sweetalert";
  export default {
    components: { ProductBox },
    data() {
      return {
        token: null,
        products: [],
        id: null,
      };
    },
    props: ["baseURL"],
    methods: {
      fetchWishList() {
        axios
          .get(`${this.baseURL}wishlist/${this.token}`)
          .then((response) => {
            this.wishlists = response.data;
            this.wishlists.forEach((wishlist) => {
              this.id = wishlist.id
              this.products.push({
                id: wishlist.product.id,
                imageURL: wishlist.product.imageURL,
                price: wishlist.product.price,
                description: wishlist.product.description,
              });
            });
          })
          .catch((err) => {
            console.log("err", err);
          });
      },
      async deleteFromWishlist() {
        await axios.delete(`${this.baseURL}wishlist/delete/${this.id}?token=${this.token}`)
            .then(() => {
              swal({
                text: "Karte von der Wunschliste entfernt",
                icon: "success"
              }).then(() => {
                this.products = [];
                this.fetchWishList();
              })
            }).catch(err => console.log('err', err));
      },
    },
    mounted() {
      this.token = localStorage.getItem("token");
      this.fetchWishList();
    },
  };
  </script>