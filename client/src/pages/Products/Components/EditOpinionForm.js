import React from 'react';
import axios from 'axios';
import { getFormDataJsonFromEvent, doRequest } from '../../../Utils';

const EditOpinionForm = ({ opinion, auth, onFinishEditCb }) => {

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        formData.opinionId = opinion.id;

        try {
            const action = async () => axios.put('/api/opinions', formData, {
                validateStatus: false,
                headers: { 'X-Auth-Token': auth.token }
            });
            await doRequest(action);
            onFinishEditCb(formData.content)
        } catch (error) {
            alert(`${error} error occured`);
        }
    };

    return <>
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <textarea name="content" className="form-control w-50" rows="3" 
                defaultValue={opinion.content} required>
                </textarea>
            </div>
            <button className="btn btn-primary px-5">
                Submit
            </button>
        </form>
    </>
};

export default EditOpinionForm;