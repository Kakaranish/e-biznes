import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import { doRequest } from '../../Utils';

const NotificationsPage = (props) => {

    const [state, setState] = useState({ loading: true, notifs: [] });
    useEffect(() => {
        const fetchNotifs = async () => {

            try {
                const action = async () => axios.get('/api/notifications/user/unread', {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                const result = await doRequest(action);
                setState({ loading: false, notifs: result });

                const nextAction = async () => axios.post('/api/notifications/user', {}, {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                await doRequest(nextAction);
                props.clearNotifs();
            } catch (error) {
                alert(`${error} error occured`);
            }
        };

        fetchNotifs();
    }, []);

    if (state.isLoading) return <></>
    else if (state.notifs?.length === 0) return <h3>You have no unread notifications</h3>
    else return <>
        <h3>Your notifications</h3>

        {
            state.notifs.map((notif, i) =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`n-${i}`}>
                    <h3>Notification {i + 1}</h3>
                    <p>{notif.content}</p>
                </div>
            )
        }
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withNotifsAwareness()
    .build(NotificationsPage);