import React, { useEffect } from 'react';
import axios from 'axios';
import AwareComponentBuilder from '../common/AwareComponentBuilder';
import { doRequest } from '../common/Utils';

const NotificationFetcher = (props) => {

    let interval;

    let fetchNotifs = async () => {
        const token = props.auth?.token;
        if (!token) {
            if (!props.notifs.length > 0) props.setNotifs([]);
            return;
        }

        try {
            const action = async () => axios.get('/api/notifications/user/unread', {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            const result = await doRequest(action);
            props.setNotifs(result);
        } catch (error) {
            alert(`${error} error occured`);
        }
    };

    useEffect(() => {
        fetchNotifs();

        interval = setInterval(() => {
            fetchNotifs();
        }, 10 * 1000);

        return () => { clearInterval(interval) };
    }, [])

    return <></>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withNotifsAwareness()
    .build(NotificationFetcher);