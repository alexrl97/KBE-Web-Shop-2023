<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center" style="margin-top: 40px">
        <h5 style="font-weight: 300; color: #686868"> {{msg}} </h5>
      </div>
    </div>
    <div class="row">
      <div v-for="product in product" :key="product.id"
           class="col-lg-2 col-md-3 col-sm-4 col-6 pt-3 justify-content-around d-flex">
        <ProductBox :product="product"/>
      </div>
    </div>
  </div>
</template>
<script>
import ProductBox from "../../components/ProductBox";
export default {
  components: {ProductBox},
  props:["categories", "products"],

  data() {
    return {
      name: null,
      categoryId: null,
      product: {},
      msg: '',
    };
  },
  mounted() {
    this.name = this.$route.params.name;
    this.product = this.products.filter(product => product.name.toLowerCase().includes(this.name.toLowerCase()));
    this.categoryId = this.$route.params.categoryId;
    if(this.categoryId > 0){
      this.product = this.product.filter(product => product.categoryId == this.categoryId);
    }

    if (this.product.length == 0) {
      this.msg = "Keine Produkte gefunden"
    } else if (this.product.length == 1) {
      this.msg = "1 Produkt gefunden"
    } else {
      this.msg = this.product.length + " Produkte gefunden"
    }
  },
}
</script>