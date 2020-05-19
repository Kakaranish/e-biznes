import React, { useState, useEffect } from 'react';
import { Route } from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import MainLayout from '../routes/MainLayout'

const AuthorizedOnlyRoute = ({ component: Component, ...rest }) => {
    const history = useHistory();
    const roles = (rest.roles ?? []).map(role => role.toUpperCase());

    const [state, setState] = useState({ loading: true, user: null });

    useEffect(() => {
        const auth = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                alert('This page requires to be logged in. Redirecting to login page...');
                history.push('/auth/login');
                return;
            };
            const result = await axios.post('/auth/verify', {}, {
                headers: { 'X-Auth-Token': localStorage.getItem('token') },
                validateStatus: false
            });
            if (result.status !== 200 && result.status !== 500) {
                alert('Internal error. Try to refresh page.');
                return;
            }

            const user = result.status === 200 ? result.data : null;
            if (!user) {
                alert('This page requires to be logged in. Redirecting to login page...');
                history.push('/auth/login');
                return;
            }
            const isAuthorized = roles.length === 0 || roles.includes(user?.role);
            if (!isAuthorized) {
                alert(`Set of allowed roles is [${roles.join(',')}]. Your is ${user.role}`);
                history.push('/');
                return;
            }
            setState({ loading: false, user: user });
        };
        auth();
    }, []);

    if (state.loading) return <></>
    else return <Route {...rest} render={matchProps => (
        <MainLayout user={state.user}>
            <Component {...matchProps} />
        </MainLayout>
    )} />
};

export default AuthorizedOnlyRoute;