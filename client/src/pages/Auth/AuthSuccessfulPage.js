import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import queryString from 'query-string';
import axios from 'axios';
import { doRequest } from '../../common/Utils';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';

const AuthSuccessfulPage = (props) => {

    const history = useHistory();
    const queryParams = queryString.parse(props.location.search);

    const auth = {
        token: queryParams.token,
        tokenExpiry: parseInt(queryParams.tokenExpiry),
        email: queryParams.email,
        role: queryParams.role
    };

    useEffect(() => {
        const performLogIn = async () => {
            const action = async () => axios.get('/api/cart/raw', {
                headers: { 'X-Auth-Token': auth.token },
                validateStatus: false
            });
            try {
                const cartItemsResult = await doRequest(action);
                props.setCartItems(cartItemsResult.map(x => x.productId));
                props.logIn(auth);
                history.push('/');
            } catch (error) {
                alert(error.msg);
            }
        };
        performLogIn();
    }, []);

    return <></>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withCartAwareness()
    .build(AuthSuccessfulPage);