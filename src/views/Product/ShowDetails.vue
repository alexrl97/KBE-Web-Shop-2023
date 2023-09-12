<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <!--            display image-->
      <div class="col-md-4 col-12">
        <img :src="product.imageURL" class="img-fluid" />
      </div>
      <!--            display product details-->
      <div class="col-md-6 col-12 pt-3 pt-md-0">
        <h4>{{ product.name }}</h4>
        <h6 class="catgory font-italic">{{ category.categoryName }}</h6>
        <h6 class="font-weight-bold">{{ product.price }} €</h6>
        <p>
          {{ product.description }}
        </p>
        <div class="d-flex flex-row justify-content-between">
          <div class="input-group col-md-3 col-4 p-0">
            <div class="input-group-prepend">
              <span class="input-group-text">Anzahl</span>
            </div>
            <input type="number" class="form-control" v-model="quantity" />
          </div>

          <div class="input-group col-md-3 col-4 p-0">
            <button
              class="btn"
              type="button"
              id="add-to-cart-button"
              @click="addToCart"
            >
              {{ cardString }}
            </button>
            <button
                id="wishlist-button"
                class="btn"
                @click="addToWishlist()"
            >
              {{ wishListString }}
            </button>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>
<script>
    import swal from "sweetalert";
import axios from "axios";
export default {
  data() {
    return {
      product: {},
      category: {},
      quantity: 1,
      wishListString: "Wunschliste hinzufügen",
      cardString: "Warenkorb hinzufügen"
    };
  },
  props: ["baseURL", "products", "categories"],
  methods: {
    addToWishlist() {
      if (!this.token) {
        // user is not logged in
        // show some error
        swal({
          text: "Bitte einloggen um Karten der Wunschliste hinzuzufügen",
          icon: "error",
        });
        return;
      }
      // add item to wishlist
      axios
        .post(`${this.baseURL}wishlist/add?token=${this.token}`, {
          id: this.product.id,
        })
        .then((res) => {
          if (res.status === 201) {
            this.wishListString = "Zur Wunschliste hinzugefügt";
            swal({
              text: "Zur Wunschliste hinzugefügt",
              icon: "success",
            });
          }
        })
        .catch((err) => {
          console.log("err", err);
        });
    },
    addToCart() {
      if (!this.token) {
        swal({
          text: "Bitte einloggen um Karten dem Warenkorb hinzuzufügen",
          icon: "error",
        });
        return;
      }
      // add to cart
      axios
        .post(`${this.baseURL}/cart/add?token=${this.token}`, {
          productId: this.id,
          quantity: this.quantity,
        })
        .then((res) => {
          if (res.status == 201) {
            swal({
              text: "Karte dem Warenkorb hinzugefügt",
              icon: "success",
            }).then(() => {
              this.$emit("fetchData");
            });
          }
        })
        .catch((err) => console.log("err", err));
    },
  },
  mounted() {
    this.id = this.$route.params.id;
    this.product = this.products.find((product) => product.id == this.id);
    this.category = this.categories.find(
      (category) => category.id == this.product.categoryId
    );
    this.token = localStorage.getItem("token");
  },
};
</script>
<style>
    .category {
        font-weight: 400;
    }
    #wishlist-button {
        margin-top: 10px;
        background-color: #bbff9e;
    }
    #add-to-cart-button {
        background-color: #febd69;
    }
</style>