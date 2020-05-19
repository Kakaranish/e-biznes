import React, { useState, useEffect } from 'react';
import { Route, useHistory } from 'react-router-dom';
import axios from 'axios';
import MainLayout from '../routes/MainLayout'

const NotAuthorizedOnlyRoute = ({ component: Component, ...rest }) => {

    const history = useHistory();
    const [state, setState] = useState({ loading: true, user: null });

    useEffect(() => {
        const auth = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                setState({ loading: false, user: null });
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
            if (user) {
                alert('This page requires not to be logged in. Redirecting to main page...');
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

export default NotAuthorizedOnlyRoute;