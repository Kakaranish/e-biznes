import React, { useEffect, useState } from 'react';
import MainLayout from './MainLayout';
import { Route } from 'react-router-dom';
import { verifyAuthState } from '../pages/Utils';
import axios from 'axios';

const MainLayoutRoute = ({ component: Component, ...rest }) => {

    const [state, setState] = useState({ loading: true, user: null });
    useEffect(() => {
        const auth = async () => {
            const user = await verifyAuthState();
            
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