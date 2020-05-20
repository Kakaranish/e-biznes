import React, { useEffect, useState } from 'react';
import MainLayout from './MainLayout';
import { Route } from 'react-router-dom';
import axios from 'axios';

const MainLayoutRoute = ({ component: Component, ...rest }) => {

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
            setState({ loading: false, user: user });
        };
        auth();
    }, []);

    if (state.loading) return <></>
    return (
        <Route {...rest} render={matchProps => (
            <MainLayout user={state.user}>
                <Component {...matchProps} user={state.user} />
            </MainLayout>
        )} />
    );
};

export default MainLayoutRoute;