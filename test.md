```js
import { getPostFromLogin } from "../../api/personnel_management_people/personnel_management_people";

export default {
  inserted(el, binding, vnode) {
    const { value } = binding
    const super_admin = "admin";

    getPostFromLogin().then(res => {
        const roles = [res];
        if (value && value instanceof Array && value.length > 0) {
            const roleFlag = value
      
            const hasRole = roles.some(role => {
              return super_admin === role || roleFlag.includes(role)
            })
      
            if (!hasRole) {
              el.parentNode && el.parentNode.removeChild(el)
            }
          } 
        })

    
    

    
}
}
```

