
<script setup>



import navaigationBar from './components/navigationBar.vue'

import {ref, computed, onBeforeMount} from 'vue'
import home from './components/home.vue'
import login from './components/login.vue'
import registration from './components/registration.vue'
import userdata from './components/userdata.vue'
import { statusStore } from './statusStore'





const routes = {
'/': home,
'/login': login,
'/registration': registration,
'/userdata': userdata,

};

// hash contain things like #....
const currentPath = ref(window.location.hash);
window.addEventListener('hashchange', () => {

    let tempValue = window.location.hash;

    if(tempValue === '#/userdata' && statusStore.isLogin === false)
    {
        window.location.hash = '#/login';
    }
    else
    {
        currentPath.value = tempValue;

    }
});

console.log("currentPath = " + currentPath.value);

const currentView = computed(() => {
return routes[currentPath.value.slice(1) || '/'];
});





function getCsrf()
{

    console.log("call getCsrf");

    fetch(import.meta.env.VITE_SERVER_BASE_URL, 
    {
         method: "POST", 
         credentials: "include",
    });


}






function getLoginStatus()
{

    
    const statusPromise = fetch(import.meta.env.VITE_SERVER_BASE_URL + "/loginStatus",
    {
        credentials: "include",
        
    
    });


    statusPromise.then( response =>    
    {

        console.log(response.status);
        const jsonPromise = response.json();

        jsonPromise.then(json => 
        {
            console.log(json.isLogin);

            if(json.isLogin === "true")
            {
                console.log("after set = " + statusStore.isLogin);

                // can not set to isLogin directly due to it is a String
                // need a boolean
                statusStore.setLoginState(true);


            }
        });
    
            
        
    }
    );



}

// get login status from server in case app was refresh lost the status
onBeforeMount(getLoginStatus);
</script>

<template>

<navaigationBar />
<component :is="currentView" />

</template>
