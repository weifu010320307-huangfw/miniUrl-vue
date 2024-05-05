
<script setup>
import { computed, ref } from 'vue';
import { statusStore } from '../statusStore';


const registerationMessage = ref("");
const formValidateMessage = ref("");

const userName = ref("");


const isRegisterDisabled = ref(false);
const checkNameMessage = ref("");

const isUseDefaultColor = ref(true);
const useMessageClass = computed(
  () => {

    return isUseDefaultColor.value ? "message" : "error";
  }
);

const isUseDefaultStyle = ref(false);
const useBlockStyle = computed(
  () => {

    return isUseDefaultStyle.value ? "display:block" : "display:none";
  }
);




function checkNameExistence()
{




  console.log("call checkNameExistence..., userName is " + userName.value);

  if(userName.value)
  {

    const fetchPromise = fetch(import.meta.env.VITE_SERVER_BASE_URL + '/registration/userStatus?name=' + userName.value);

    fetchPromise.then( 
      response => 
      {
        if(response.status == 200)
        {
          console.log("response.status is " + response.status + response);
  
          const jsonPromise = response.json();
          jsonPromise.then( json => {
            console.log(json);

            isUseDefaultStyle.value = true;
  
            if(json.userExist == "YES")
            {
              console.log("The username has been used");

              checkNameMessage.value = "The username has been used";
              isUseDefaultColor.value = false;
              isRegisterDisabled.value = true;

            }
            else
            {
              console.log("The username is available");

              checkNameMessage.value = "The username is available";
              isUseDefaultColor.value = true;
              isRegisterDisabled.value = false;
            }
          });
        }
        else
        {
          console.log("response.status is " + response.status);
        }
      });
  }
  else
  {


    checkNameMessage.value = "";
    isUseDefaultStyle.value = false;

  }


}

const password = ref("");
const confirmPassword = ref("");


function registration()
{
    const registrationData = new FormData();
    registrationData.append("userName", userName.value);
    registrationData.append("password", password.value);
    registrationData.append("confirmPassword", confirmPassword.value);


    const csrfToken = document.cookie.replace(
                    /(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/,
                    "$1",
                    );

    fetch(import.meta.env.VITE_SERVER_BASE_URL + "/registration", 
    {
      method: "POST", 
      body: registrationData,
      credentials: "include",
      headers: {
        "X-XSRF-TOKEN": csrfToken,
      }
    })
    .then(response => {  
   
      const status = response.status;
      const jsonPromise = response.json(); 

      jsonPromise.then(data => processRegistrationData(data, status))

    }) 
    .catch((error) => {
      console.error("exceptionError:", error);
    });

}

function processRegistrationData(data, status)
{
  if(status === 201)
  {
    console.log("Success:", data);


    if(data.redirectUrl)
    {

      statusStore.isLogin = true;

      window.location.hash = "#/" + data.redirectUrl;
    }
    
    // inform user register failure
    if(data.registerResult)
    {

      registerationMessage.value = data.registerResult;
    }

  }
  else
  {
    console.log("Registration ErrorMessage: ", data);

    formValidateMessage.value = data[0].defaultMessage;


  }
}


function resetRegistration()
{
  checkNameMessage.value = "";
  registerationMessage.value = "";
  isRegisterDisabled.value = false;
  formValidateMessage.value = "";

}

</script>

<template>
      <form class="formClass" autocomplete="on" @submit.prevent="registration()">
      <h2>Fill in to register an account</h2>
      <label class="italicLabel" for="userName">User Name</label>
      <input id="userName" required aria-required="true" @blur="checkNameExistence()" autocomplete="on" v-model="userName"/> 

      <p id="checkUser" :class="useMessageClass" :style="useBlockStyle" role="alert" aria-relevant="all" > {{ checkNameMessage }}</p>


      <label class="italicLabel" for="password">Password</label>
      <input id="password" type="password" required aria-required="true" autocomplete="on" v-model="password"/>


      <label class="italicLabel" for="confirmPassword">Confirm Password</label>
      <input id="confirmPassword" type="password" required aria-required="true" autocomplete="on" v-model="confirmPassword"/>    
      <p class="error" th:each="error : ${#fields.errors('confirmPassword')}" 
      th:text="${error}" role="alert" aria-relevant="all" v-if="formValidateMessage">{{ formValidateMessage }}</p>  

      <button id="registrationButton" :disabled="isRegisterDisabled" type="submit" class="buttonClass">Registration</button>
      <button id="resetRegister" class="buttonClass" type="reset" @click="resetRegistration()"><i>Reset</i></button>
      <p class="registrationOrLogin"><a href="#/login">Sign in if already has an account</a></p>
      <p class="error" v-if="registerationMessage">{{ registerationMessage }}</p>

  </form>
</template>

<style scoped>
@import '../assets/main.css';
</style>