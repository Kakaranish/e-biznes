import React, { useEffect } from 'react';
import axios from 'axios';
import AwareComponentBuilder from '../common/AwareComponentBuilder';

const NotificationFetcher = (props) => {

    let interval;
    
    let action = async () => {
        const token = props.auth?.token;
        if(!token) {
            if(!props.notifs.length > 0) props.setNotifs([]);
            return;
        }

        const result = await axios.get('/api/notifications/user/unread', {
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false
        });

        if (result.status !== 200) {
            console.log("some error occured");
            console.log(result.data);
            return;
        }

        props.setNotifs(result.data);
    };

    useEffect(() => {
        action();
        
        interval = setInterval(() => {
            action();
        }, 10 * 1000);

        return () => { clearInterval(interval) };
    }, [])

    return <></>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withNotifsAwareness()
    .build(NotificationFetcher);