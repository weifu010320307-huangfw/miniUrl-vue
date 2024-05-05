
function showShortenResult(event) {

  event.preventDefault();
  const shortResult = document.querySelector("#shortenResult");
  const shortenRatio = document.querySelector("#shortenRatio");
  const accessRatio = document.querySelector("#accessRatio");

  shortResult.style.setProperty("visibility", "visible");


}

function updateUIWhenInputChange() 
{
  const shortResult = document.querySelector("#shortenResult");
  const shortenRatio = document.querySelector("#shortenRatio");
  const accessRatio = document.querySelector("#accessRatio");

  const emptyError = document.querySelector("#emptyError");
  const signInLink = document.querySelector("#signInLink");

  
 

  if(shortResult)
  {

    shortResult.style.setProperty("visibility", "hidden");
  }
  

  if(shortenRatio)
  {
    shortenRatio.style.setProperty("visibility", "hidden");

  }

  if(accessRatio)
  {
    accessRatio.style.setProperty("visibility", "hidden");
  }
  

  if(emptyError)
  {
    emptyError.style.setProperty("visibility", "hidden");

  }

  if(signInLink)
  {
    signInLink.style.setProperty("visibility", "hidden");

  }


  
  
}



function checkNameExistence()
{

  const userName = document.querySelector("#userName").value;
  const checkUser = document.querySelector("#checkUser");
  const registrationButton = document.querySelector("#registrationButton");
  console.log("call checkNameExistence..., userName is " + userName);

  if(userName && checkUser)
  {

    const fetchPromise = fetch('/registration/userStatus?name=' + userName);

    fetchPromise.then( 
      response => 
      {
        if(response.status == 200)
        {
          console.log("response.status is " + response.status + response);
  
          const jsonPromise = response.json();
          jsonPromise.then( json => {
            console.log(json);

            checkUser.style = "display:block";
  
            if(json.userExist == "YES")
            {
              console.log("The username has been used");
              checkUser.textContent = "The username has been used";
              checkUser.className = "error";
              registrationButton.disabled = true;
            }
            else
            {
              console.log("The username is available");
              checkUser.textContent = "The username is available";
              checkUser.className = "message";
              registrationButton.disabled = false;
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
    checkUser.innerHTML = "";
    checkUser.style = "display:none";

  }


}

function logout()
{

  const logoutForm = document.querySelector("#logoutForm");

  logoutForm.submit();


  
}


function updateRegisterationUI()
{

  const checkUser = document.querySelector("#checkUser");

  if(checkUser)
  {
    checkUser.textContent = "";

  }
}

function resetIndexPage()
{
  const shortUrlInput = document.querySelector("#shortUrlInput");
  if(shortUrlInput)
  {

    shortUrlInput.value = "";
    updateUIWhenInputChange();

  }
}

function resetRegisterPage()
{
  const userName = document.querySelector("#userName");
  const password = document.querySelector("#password");
  const confimPassword = document.querySelector("#confirmPassword");
  const checkUser = document.querySelector("#checkUser");
  const checkUserError = document.querySelector("#checkUser.error");
  const registrationButton = document.querySelector("#registrationButton");

  if(userName)
  {
    userName.value = "";
  }



  if(password)
  {
    password.value = "";
  }

  if(confimPassword)
  {
    confimPassword.value = "";
  }

  if(checkUser)
  {
    console.log("checkUser display none..");
    checkUser.style = "display:none";
  }

  if(checkUserError)
  {
    checkUserError.style = "display:none;";
  }

  if(registrationButton)
  {
    registrationButton.disabled = false;
  }

}

function resetLoginPage()
{

  const username = document.querySelector("#username");
  const password = document.querySelector("#password");
  const error = document.querySelector(".error");
  const message = document.querySelector(".message");

  if(username)
  {
    username.value = "";
  }

  if(password)
  {
    password.value = "";
  }

  if(error)
  {
    error.style = "display:none";
  }

  if(message)
  {
    message.style = "display:none";
  }
}


// above code are function definition
// below are event binding
// move to here due to content security policy

// proccess logout event
const logoutLink = document.querySelector(".logoutLink");
if(logoutLink)
{
  logoutLink.addEventListener("click", logout);
}


// proccess ui update when user input
const shortUrlInput = document.querySelector("#shortUrlInput");
if(shortUrlInput)
{
  shortUrlInput.addEventListener("input", updateUIWhenInputChange);
}


// check if username already exist
const userName = document.querySelector("#userName");
if(userName)
{
  userName.addEventListener("blur", checkNameExistence);
  userName.addEventListener("input", updateRegisterationUI);
}



const resetButton = document.querySelector("#resetButton");
if(resetButton)
{
  resetButton.addEventListener("click", resetIndexPage);
}



const resetLogin = document.querySelector("#resetLogin");
if(resetLogin)
{
  resetLogin.addEventListener("click", resetLoginPage);
}

const resetRegister = document.querySelector("#resetRegister");
if(resetRegister)
{
  resetRegister.addEventListener("click", resetRegisterPage);
}