<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h3 class="pt-3">Kartentyp hinzufügen</h3>
      </div>
    </div>
    <div class="row">
      <div class="col-3"></div>
      <div class="col-6">
        <form>
          <div class="form-group">
            <label>Name</label>
            <input type="text" class="form-control" v-model="categoryName" />
          </div>
          <div class="form-group">
            <label>Beschreibung</label>
            <input type="text" class="form-control" v-model="description" />
          </div>
          <div class="form-group">
            <label>Bild URL</label>
            <input type="text" class="form-control" v-model="imageUrl" />
          </div>
          <button type="button" class="btn btn-primary" @click="addCategory">
            Kartentyp hinzufügen
          </button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>
<script>
const axios = require("axios");
const sweetalert = require("sweetalert");
export default {
  data() {
    return {
      categoryName: "",
      description: "",
      imageUrl: "",
    };
  },
  methods: {
    addCategory() {
      console.log(this.categoryName, this.description);
      const newCategory = {
        categoryName: this.categoryName,
        description: this.description,
        imageUrl: this.imageUrl,
      };

      const baseURL = "http://localhost:8080/";

      axios({
        method: "post",
        url: `${baseURL}/category/create?token=${this.token}`,
        data: JSON.stringify(newCategory),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then(() => {
          sweetalert({
            text: "Kartentyp hinzugefügt",
            icon: "success",
          }).then(() => {
            this.$emit("fetchData");
            this.$router.push({name: 'AdminCategory'})
          });
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },

  mounted() {
    this.token = localStorage.getItem("token");
  },
};
</script>
<style scoped></style>