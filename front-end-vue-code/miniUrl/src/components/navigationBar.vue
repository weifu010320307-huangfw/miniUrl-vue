<script setup>

import { ref } from 'vue';
import { statusStore } from '../statusStore';

const logoutForm = ref();

function logout()
{
  console.log("call logout");

  logoutForm.submit = postToLogout();


  
}

function postToLogout()
{

  const csrfToken = document.cookie.replace(
                    /(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/,
                    "$1",
                    );

  console.log("calll post to logout, csrfToken = " + csrfToken);


  fetch(import.meta.env.VITE_SERVER_BASE_URL + "/logout", {
  method: "POST", 
  headers: {
        "X-XSRF-TOKEN": csrfToken,
  },
  credentials: "include",


  })
  .then((response) => 
  {

    console.log(response);

    if(response.url && response.url.indexOf("?logout") != -1)
    {



      window.location.hash = "#/login";

      statusStore.setLogoutState(true);
      statusStore.setLoginState(false);

    }

  })
  .catch((error) => {
    console.error("Error:", error);
  });
}






</script>

<template>
    <nav>
        <header>
            <a href="#/">miniUrl</a>
        </header> 
        <ul>

            <li><a href="#/">Home</a></li>
            <li><a href="#/userdata">Management</a></li>
            <li><a href="#/registration">Registration</a></li>
            <li v-if="statusStore.isLogin === false"><a class="logoutLink" href="#/login" >Login</a></li>
            <li v-if="statusStore.isLogin === true"><a class="logoutLink" @click="logout">Logout</a></li>
           

        </ul>

    </nav>

    <form ref="logoutForm">
    </form>


</template>

<style scoped>


*
{

  box-sizing: content-box;
}

header, nav
{
  padding: 1%;

}



nav
{

  height: 5rem;
  display: flex;
  align-items: stretch;

  background-image: linear-gradient(  rgba(0, 255, 170, 0.9), rgba(0, 234, 255, 0.5));
}

nav ul
{
  height: 5rem;
  flex: 1;
  display: flex;
  justify-items: flex-end;
  align-items: stretch;
  list-style-type: none;
  padding: 0;
  
}

ul li
{

  display: inline;
  flex: min-content;
  text-align: right;
  padding: 0.3rem;

}



nav a {
  display: inline-block;
  font-size: 2rem;
  text-decoration: none;
  color: white;

}

nav a:hover
{
  color: blue;

}




Nav header
{
  flex: 2;
  display: flex;
  align-items: center;
}

Nav header a
{
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  font-size: xx-large;
  text-shadow: 1px 1px 2px red, 0 0 1em blue, 0 0 0.2em blue;

}

</style>