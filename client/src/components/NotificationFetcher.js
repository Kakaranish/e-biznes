import React, { useEffect } from 'react';
import { createAuthAndNotifAwareComponent } from '../Utils';
import axios from 'axios';

const NotificationFetcher = (props) => {

    let interval;
    
    let action = async () => {
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

export default createAuthAndNotifAwareComponent(NotificationFetcher);