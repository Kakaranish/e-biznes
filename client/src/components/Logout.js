import React from 'react';
import * as AuthUtils from '../pages/Auth/Utils';

const Logout = (props) => {

    const onClick = () => {
        if (!props.auth) {
            alert('Cannot logout because no one is logged in.');
            return;
        }

        localStorage.removeItem('auth');
        props.logOut();
    };

    return <div onClick={onClick}>
        Log out
    </div>
};

export default AuthUtils.createAuthAwareComponent(Logout);