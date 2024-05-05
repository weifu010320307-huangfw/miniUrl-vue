
import { reactive } from "vue";

export const statusStore = reactive(
    {

        isLogin: false,
        isLogout: false,

        setLoginState(state)
        {
            this.isLogin = state;
        },

       
        setLogoutState(state)
        {

            this.isLogout = state;
        }
    }
) 