<template>
  <div id="home">
    <div id="background-div" class="page-holder bg-cover">
      <div class="container py-5">
      </div>
      <hr />
    </div>

    <!--    display categories-->
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h2 class="pt-4 mt-4">Kartentypen</h2>
        </div>
      </div>
      <div class="row">
        <div v-for="index in this.categorySize" :key="index"
             class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex">
          <CategoryBox :category="categories[index-1]" />
        </div>
      </div>
    </div>
    <div class="container py-2">
      <div class="row">
        <div class="col-12 text-center">
          <h2 class="pt-4 mt-4">Karten</h2>
        </div>
      </div>

      <!--      display products-->
      <div class="row">
        <div v-for="index in this.productSize" :key="index" class="col-lg-2 col-md-3 col-sm-4 col-6 pt-3 justify-content-around d-flex">
          <ProductBox :product="products[index-1]" />
        </div>
    </div>
    </div>
  </div>
</template>

<script>
import CategoryBox from "../components/CategoryBox";
import ProductBox from "../components/ProductBox";
export default {
  name: "HomeView",
  components: {ProductBox, CategoryBox},
  props: ["categories", "products"],
  data() {
    return {
      categorySize: 0,
      productSize: 0
    }
  },
  mounted() {
    this.categorySize = Math.min(this.categories.length );
    this.productSize = Math.min(50,this.products.length);
  }
};
</script>
<style>
  .page-holder {
    min-height: 50vh;
  }
  .bg-cover {
  background-size: cover !important;
}
  #background-div {
    background: url("../assets/2.jpg");
  }
  #heading {
    font-weight: 400;
  }
</style>