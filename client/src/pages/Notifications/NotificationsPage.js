import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';

const NotificationsPage = (props) => {

    const [state, setState] = useState({ loading: true, notifs: [] });
    useEffect(() => {
        const fetchNotifs = async () => {
            const result = await axios.get('/api/notifications/user/unread', {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            if (result.status !== 200) {
                alert('Some error occured');
                console.log(result.data);
                return;
            }
            setState({ loading: false, notifs: result.data });

            await axios.post('/api/notifications/user', {}, {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            props.clearNotifs();
        };

        fetchNotifs();
    }, []);

    if (state.isLoading) return <></>
    else if (state.notifs?.length === 0) return <h3>You have no unread notifications</h3>
    else return <>
        <h3>Your notifications</h3>

        {
            state.notifs.map((n, i) =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`n-${i}`}>
                    Notification 1
                </div>
            )
        }
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withNotifsAwareness()
    .build(NotificationsPage);