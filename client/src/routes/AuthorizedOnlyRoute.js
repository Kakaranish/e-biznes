import React, { useState, useEffect } from 'react';
import { Route } from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import * as Utils from '../Utils';

const AuthorizedOnlyRoute = ({ component: Component, ...rest }) => {

    const history = useHistory();
    const roles = (rest.roles ?? []).map(role => role.toUpperCase());

    const [state, setState] = useState({ loading: true, auth: null });

    useEffect(() => {
        const auth = async () => {

            const token = rest.auth?.token;
            if (!token) {
                alert('This page requires to be logged in. Redirecting to login page...');
                history.push('/auth/login');
                return;
            };
            const result = await axios.post('/auth/verify', {}, {
                headers: { 'X-Auth-Token': token },
                validateStatus: false
            });
            if (result.status === 500) {
                alert('Internal error. Try to refresh page.');
                return;
            }
            if (result.status !== 200) {
                alert('This page requires to be logged in. Redirecting to login page...');
                rest.logOut();
                history.push('/auth/login');
                return;
            }
            const isAuthorized = roles.length === 0 || roles.includes(result.data?.role);
            if (!isAuthorized) {
                alert(`Set of allowed roles is [${roles.join(',')}]. Your is ${result.data?.role ?? "undefined"}`);
                history.push('/');
                return;
            }

            let updatedAuth = Object.assign({}, result.data);
            updatedAuth.token = token;

            if (result.data.role !== rest.auth.role || result.data.email !== rest.auth.email
                || result.data.tokenExpiry !== rest.auth.tokenExpiry) {
                rest.logIn(updatedAuth);
            }

            setState({ loading: false, auth: result.data });
        };
        auth();
    }, []);

    if (state.loading) return <></>
    else return <Route {...rest} render={matchProps => (
        <Component {...matchProps} />
    )} />
};

export default Utils.createAuthAwareComponent(AuthorizedOnlyRoute);