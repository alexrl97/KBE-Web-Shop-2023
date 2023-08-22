<template>
  <div class="alert alert-success" role="alert" id="message">
    Payment successful
  </div>
</template>

<script>
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
      axios.post(this.baseURL+"order/add/?token="+this.token+"&sessionId="+this.sessionId)
          .then(()=>{
            document.getElementById("message").innerHTML = "order placed";
          }).catch((error)=>{
        console.log(error);
      })
    }
  },
  mounted(){
    this.token = localStorage.getItem("token");
    this.sessionId = localStorage.getItem("sessionId");
    this.saveOrder()
  }
}
</script>