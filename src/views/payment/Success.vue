<template>
  <div class="alert alert-success" role="alert" id="message">
    Zahlung erfolgreich
  </div>
</template>

<script>
import swal from "sweetalert";
const axios = require('axios')
export default {
  name:'PaymentSuccess',
  props:["baseURL"],
  data() {
    return {
      token: null,
      sessionId:null
    }
  },
  methods:{
    saveOrder() {
      axios
          .post(this.baseURL + "order/add/?token=" + this.token + "&sessionId=" + this.sessionId)
          .then(() => {
            // Show the SweetAlert and capture the result
            swal({
              text: "Bestellung erhalten",
              icon: "success",
              confirmButtonText: "Okay",
            }).then(() => {
                this.$router.push({ name: "OrderHistory" });
            });
          })
          .catch((error) => {
            console.log(error);
          });
    }
  },
  mounted(){
    this.token = localStorage.getItem("token");
    this.sessionId = localStorage.getItem("sessionId");
    this.saveOrder()
  }
}
</script>