
<script setup>

import { ref } from 'vue';


const isShowShortenResult = ref(false);


const longUrl = ref("");
const shortUrl = ref("");
const shortenTimes = ref(0);
const accessTimes = ref(0);


const longUrlFieldError = ref("");

function showShortenResult(isShow)
{
  isShowShortenResult.value = isShow;
  longUrlFieldError.value = "";

}

function setShortenResult(response)
{
  console.log("setShortenResult" + " = " + response.shortUrl + " = " + response.shortenTimes +  " = " + response.accessTimes);
  shortUrl.value = response.shortUrl;
  shortenTimes.value = response.shortenTimes;
  accessTimes.value = response.accessTimes;

}

function processResponseData(data, status)
{
  if(status === 200)
  {
    console.log("processResponseData Success:", data);

    setShortenResult(data);
    showShortenResult(true);
  }
  else
  {
    console.log("ErrorMessage:", data);

    longUrlFieldError.value = data[0].defaultMessage;

  }
}

function submitUrlToShorten()
{
  const data = { longUrl: longUrl.value };

  const csrfToken = document.cookie.replace(
                    /(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/,
                    "$1",
                    );
  console.log("In submitUrlToShorten, XSRF-TOKEN = " + csrfToken);

  fetch(import.meta.env.VITE_SERVER_BASE_URL + "/shortUrl", 
  {
    method: "POST", // or 'PUT'
    headers: {
      "Content-Type": "application/json",
      "X-XSRF-TOKEN": csrfToken,
    },

    credentials: "include",
    body: JSON.stringify(data),
  })
    .then((response) => { 
      
      const status = response.status;
      const jsonPromise = response.json(); 

      jsonPromise.then(data => processResponseData(data, status))

     
       })
    .catch((error) => {
      console.error("Error:", error);
    });



}



</script>


<template>

  <form id="shortenForm" @submit.prevent="submitUrlToShorten()" autocomplete="on"> 
      <!-- <label id="shortenFormLabel" for="shortUrlInput"> <i>Enter the url to be shorten</i> </label> -->
      <label for="shortUrlInput">         
          <h2>Enter the url to be shorten</h2> 
      </label>

      <input type="url" id="shortUrlInput" required aria-required="true" name="longUrl" v-model.trim="longUrl" @input="showShortenResult(false)">

      <p id="emptyError" role="alert" aria-relevant="all" v-if="longUrlFieldError" class="error" >{{ longUrlFieldError }}</p>
      <button id="shortenNowButton" type="submit"><i>Shorten now</i></button>
      <button id="resetButton" type="reset" @click="showShortenResult(false)"><i>Reset</i></button>
      <p id="shortenResult" role="alert" aria-relevant="all" v-if="isShowShortenResult" th:if="${not #fields.hasErrors('longUrl')} and *{longUrl}">The shorten url is <a :href="shortUrl" target="_blank" ref="noopener" :text="shortUrl"> access now </a></p>
      <p id="shortenRatio" role="alert" aria-relevant="all" v-if="isShowShortenResult" th:if="*{shortenTimes}">How often was the url shorten? <span :text="shortenTimes"> {{shortenTimes}} </span> times</p>
      

      <p id="accessRatio" role="alert" aria-relevant="all" v-if="isShowShortenResult" th:if="*{shortenTimes}">How often was the shortten url accessed? <span> {{accessTimes}} </span> times</p>
      <p id="signInLink" v-if="isShowShortenResult" th:if="${not #fields.hasErrors('longUrl')} and *{longUrl}"><a href="#/login">Sign in </a>to manage all your data</p>

     
    </form>



</template>

<style scoped>
@import '../assets/main.css';

</style>
