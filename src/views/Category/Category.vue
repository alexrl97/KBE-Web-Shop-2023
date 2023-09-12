<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h3 class="pt-3">Kartentypen</h3>
        <router-link :to="{ name: 'AddCategory' }">
          <button v-if="role === 'storehouse'" class="btn" style="background-color: #aeffae; margin-top: 30px; margin-bottom: 30px;">Kartentyp hinzufügen</button>
        </router-link>
      </div>

    </div>
    <div class="row">
      <div
        v-for="category of categories"
        :key="category.id"
        class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex">
        <CategoryBox :category="category"> </CategoryBox>
      </div>
    </div>

  </div>
</template>
<script>
import CategoryBox from "../../components/CategoryBox.vue";
export default {
  name: "Category",
  props:["categories"],
  components: { CategoryBox },
  data() {
    return {
      baseURL: "http://localhost:8080/",
      role: null,
    };
  },
  methods: {
  },
  mounted() {
    this.role = localStorage.getItem("role");
  },
};
</script>
<style scoped></style>