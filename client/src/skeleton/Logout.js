import React from 'react';
import { useHistory } from 'react-router-dom';
import AwareComponentBuilder from '../common/AwareComponentBuilder';

const Logout = (props) => {

    const history = useHistory();

    const onClick = () => {
        if (!props.auth) {
            alert('Cannot logout because no one is logged in.');
            return;
        }

        props.logOut();
        props.clearCart();
        history.go();
    };

    return <div onClick={onClick}>
        Log out
    </div>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withCartAwareness()
    .build(Logout);