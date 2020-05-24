import React from 'react';
import * as Utils from '../Utils';
import { useHistory } from 'react-router-dom';

const Logout = (props) => {

    const history = useHistory();

    const onClick = () => {
        if (!props.auth) {
            alert('Cannot logout because no one is logged in.');
            return;
        }

        props.logOut();
        history.go();
    };

    return <div onClick={onClick}>
        Log out
    </div>
};

export default Utils.createAuthAwareComponent(Logout);