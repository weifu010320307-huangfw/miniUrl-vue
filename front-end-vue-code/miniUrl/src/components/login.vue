
<script setup>

import { ref } from 'vue'
import { statusStore } from '../statusStore';

const username = ref("");
const password = ref("");

const isLoginError = ref(false);



// this is different to submitUrlToShorten because the login is handle directly by spring security
// it use different protocol
function submitLogin()
{
    // const loginData = { username: username.value, password: password.value};

    const loginData = new FormData();
    loginData.append("username", username.value);
    loginData.append("password", password.value);

    const csrfToken = document.cookie.replace(
                    /(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/,
                    "$1",
                    );

    console.log("In submitLogin, csrfToken = " + csrfToken);

    fetch(import.meta.env.VITE_SERVER_BASE_URL + "/login", {
    method: "POST", // or 'PUT'
    headers: {
        "X-XSRF-TOKEN": csrfToken,
    },
    credentials: "include",
    body: loginData,
    })
    .then(response => {  
   

        console.log(response);


        if(response.url && response.url.indexOf("?error") != -1)
        {
            isLoginError.value = true;
        }
        else
        {
            window.location.hash = "#/userdata";
            statusStore.setLoginState(true);
        }

    }
      
        
     
       )

    .catch((error) => {
      console.error("Error:", error);
    });

}

function resetLoginForm()
{
    isLoginError.value = false;
    statusStore.setLogoutState(false);
}

</script>

<template>
    <form class="formClass" @submit.prevent="submitLogin()" autocomplete="on">
    
    <h2>Sign in to management and see all your url</h2>
    <label class="italicLabel" for="username"> User Name </label>
    <input type="text" id="username" name="username" v-model="username" required aria-required="true"/> 
    <label class="italicLabel" for="password" > Password </label>
    <input type="password" id="password" name="password" v-model="password" autocomplete="on" required aria-required="true"/>
    <!-- <input class="buttonClass" type="submit" value="Sign In"/> -->
    <button id="loginButton" class="buttonClass" type="submit"><i>Sign In</i></button>
    <button id="resetLogin" class="buttonClass" type="reset" @click="resetLoginForm()"><i>Reset</i></button>
    <p class="registrationOrLogin"><a href="#/registration">Or registration an account</a></p>
    <p class="error" role="alert" aria-relevant="all" v-if="isLoginError">
        Invalid username or password
    </p>
    <p class="message" role="alert" aria-relevant="all" v-if="statusStore.isLogout">
        You have been logged out
    </p>
</form>
</template>


<style scoped>
@import '../assets/main.css';

</style>